package xyz.vsngamer.elevatorid.network;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.DirectionalElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.init.ModSounds;
import xyz.vsngamer.elevatorid.init.ModTags;

import java.util.function.Supplier;

public class TeleportHandler {
    static boolean handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null) return true;

        ServerWorld world = player.getServerWorld();
        BlockPos from = message.getFrom(), to = message.getTo();

        // Integrity checks

        // TODO deprecated, change later
        if (!world.isAreaLoaded(from, to)) {
            return true;
        }

        // This ensures the player is still standing on the origin elevator
        final double distanceSq = player.getDistanceSq(new Vec3d(from).add(0, 1, 0));
        if (distanceSq > 4D) return true;

        if (from.getX() != to.getX() || from.getZ() != to.getZ()) return true;

        BlockState fromState = world.getBlockState(from);
        BlockState toState = world.getBlockState(to);
        Block toElevator = toState.getBlock();

        if (!isElevator(fromState) || !isElevator(toState)) return true;

        if (!validateTarget(world, to)) return true;

        // Check yaw and pitch
        final float yaw, pitch;
        yaw = ModTags.DIRECTIONAL_ELEVATORS_TAG.contains(toElevator) ? toState.get(DirectionalElevatorBlock.FACING).getHorizontalAngle() : player.rotationYaw;
        pitch = (ModConfig.GENERAL.resetPitchNormal.get() && ModTags.NORMAL_ELEVATORS_TAG.contains(toElevator))
                || (ModConfig.GENERAL.resetPitchDirectional.get() && ModTags.DIRECTIONAL_ELEVATORS_TAG.contains(toElevator)) ? 0F : player.rotationPitch;

        // Passed all tests, begin teleport
        ctx.get().enqueueWork(() -> {
            if (ModConfig.GENERAL.precisionTarget.get())
                player.connection.setPlayerLocation(to.getX() + 0.5D, to.getY() + 1D, to.getZ() + 0.5D, yaw, pitch);
            else
                player.connection.setPlayerLocation(player.getPosition().getX(), to.getY() + 1D, player.getPosition().getZ(), yaw, pitch);

            player.setMotion(player.getMotion().mul(new Vec3d(1, 0, 1)));
            world.playSound(null, to, ModSounds.TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
        });

        return true;
    }

    public static boolean validateTarget(IBlockReader world, BlockPos target) {
        return validateTarget(world.getBlockState(target.up(1))) && validateTarget(world.getBlockState(target.up(2)));
    }

    private static boolean validateTarget(BlockState blockState) {
        return !blockState.getMaterial().isSolid(); // TODO maybe change this
    }

    public static boolean isElevator(BlockState blockState) {
        return ModTags.ALL_ELEVATORS_TAG.contains(blockState.getBlock());
    }
}
