package xyz.vsngamer.elevatorid.network;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.init.ModSounds;

import java.util.function.Supplier;

public class TeleportHandler {

    static boolean handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null || !player.isAlive()) return;

            ServerWorld world = player.getServerWorld();
            BlockPos from = message.getFrom(), to = message.getTo();

            // This ensures the player is still standing on the origin elevator
            final double distanceSq = player.getDistanceSq(new Vector3d(from.getX(), from.getY(), from.getZ()).add(0, 1, 0));
            if (distanceSq > 4D) return;

//        double dist = from.distanceSq(to.getX(), to.getY(), to.getZ(), false);
//        if (dist > Math.pow(ModConfig.GENERAL.range.get(), 2)) return;

            if (from.getX() != to.getX() || from.getZ() != to.getZ()) return;

            BlockState fromState = world.getBlockState(from);
            BlockState toState = world.getBlockState(to);

            if (!isElevator(fromState) || !isElevator(toState)) return;

            if (!validateTarget(world, to)) return;

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
            if (ModConfig.GENERAL.useXP.get() && !player.isCreative()) {
                if (getPlayerExperienceProgress(player) - ModConfig.GENERAL.XPPointsAmount.get() >= 0 || player.experienceLevel > 0) {
                    player.giveExperiencePoints(-ModConfig.GENERAL.XPPointsAmount.get());
                } else {
                    player.sendMessage(new TranslationTextComponent("elevatorid.message.missing_xp").mergeStyle(TextFormatting.RED), player.getUniqueID());
                    return;
                }
            }

            // Teleport prevents sync issues when riding entities
            double blockYOffset = toState.getCollisionShape(world, to).getEnd(Direction.Axis.Y);
            player.teleport(world, toX, to.getY() + (blockYOffset == Double.NEGATIVE_INFINITY ? 1 : blockYOffset), toZ, yaw, pitch);
            player.setMotion(player.getMotion().mul(new Vector3d(1D, 0D, 1D)));
            world.playSound(null, to, ModSounds.TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
        });

        return true;
    }

    private static int getPlayerExperienceProgress(PlayerEntity player) {
        return Math.round(player.experience * player.xpBarCap());
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
