package xyz.vsngamer.elevatorid.network;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.init.ModSounds;

import java.util.function.Supplier;

public class TeleportHandler {

    static boolean handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null) return true;

        ServerWorld world = player.getServerWorld();
        BlockPos from = message.getFrom(), to = message.getTo();

        // This ensures the player is still standing on the origin elevator
        final double distanceSq = player.getDistanceSq(new Vec3d(from).add(0, 1, 0));
        if (distanceSq > 4D) return true;

//        double dist = from.distanceSq(to.getX(), to.getY(), to.getZ(), false);
//        if (dist > Math.pow(ModConfig.GENERAL.range.get(), 2)) return;

        if (from.getX() != to.getX() || from.getZ() != to.getZ()) return true;

        BlockState fromState = world.getBlockState(from);
        BlockState toState = world.getBlockState(to);

        if (!isElevator(fromState) || !isElevator(toState)) return true;

        if (!validateTarget(world, to)) return true;

        // Check yaw and pitch
        final float yaw, pitch;
        yaw = toState.get(ElevatorBlock.DIRECTIONAL)
                ? toState.get(ElevatorBlock.HORIZONTAL_FACING).getHorizontalAngle() : player.rotationYaw;

        pitch = (toState.get(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchDirectional.get())
                || (!toState.get(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchNormal.get())
                ? 0F : player.rotationPitch;

        // Set X and Z
        final double toX, toZ;
        if (ModConfig.GENERAL.precisionTarget.get()) {
            toX = to.getX() + .5D;
            toZ = to.getZ() + .5D;
        } else {
            toX = player.getPosX();
            toZ = player.getPosZ();
        }

        // Passed all tests, begin teleport
        ctx.get().enqueueWork(() -> {
            player.connection.setPlayerLocation(toX, to.getY() + 1D, toZ, yaw, pitch);
            player.onGround = true;
            player.setMotion(player.getMotion().mul(new Vec3d(1D, 0D, 1D)));

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
        return blockState.getBlock() instanceof ElevatorBlock;
    }
}
