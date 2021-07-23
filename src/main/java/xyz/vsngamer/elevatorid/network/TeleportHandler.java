package xyz.vsngamer.elevatorid.network;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.init.ModSounds;

import java.util.function.Supplier;

public class TeleportHandler {
    static void handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !player.isAlive()) return;

            ServerLevel world = player.getLevel();
            BlockPos from = message.getFrom(), to = message.getTo();

            if (!world.isLoaded(from) || !world.isLoaded(to))
                return;

            // This ensures the player is still standing on the "from" elevator
            final double distanceSq = player.distanceToSqr(new Vec3(from.getX(), from.getY(), from.getZ()).add(0, 1, 0));
            if (distanceSq > 4D) return;

            if (from.getX() != to.getX() || from.getZ() != to.getZ()) return;

            BlockState fromState = world.getBlockState(from);
            BlockState toState = world.getBlockState(to);

            if (!isElevator(fromState) || !isElevator(toState)) return;

            if (!validateTarget(world, to)) return;

            // Check yaw and pitch
            final float yaw, pitch;
            yaw = toState.getValue(ElevatorBlock.DIRECTIONAL)
                    ? toState.getValue(ElevatorBlock.FACING).toYRot() : player.getYRot();

            pitch = (toState.getValue(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchDirectional.get())
                    || (!toState.getValue(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchNormal.get())
                    ? 0F : player.getXRot();

            // Set X and Z
            final double toX, toZ;
            if (ModConfig.GENERAL.precisionTarget.get()) {
                toX = to.getX() + .5D;
                toZ = to.getZ() + .5D;
            } else {
                toX = player.getX();
                toZ = player.getZ();
            }

            // XP
            if (ModConfig.GENERAL.useXP.get() && !player.isCreative()) {
                if (getPlayerExperienceProgress(player) - ModConfig.GENERAL.XPPointsAmount.get() >= 0 || player.experienceLevel > 0) {
                    player.giveExperiencePoints(-ModConfig.GENERAL.XPPointsAmount.get());
                } else {
                    player.sendMessage(
                            new TranslatableComponent("elevatorid.message.missing_xp")
                                    .withStyle(ChatFormatting.RED), player.getUUID()
                    );
                    return;
                }
            }

            // Passed all tests, begin teleport
            double blockYOffset = toState.getBlockSupportShape(world, to).max(Direction.Axis.Y);
            player.teleportTo(world, toX, to.getY() + (blockYOffset == Double.NEGATIVE_INFINITY ? 1 : blockYOffset), toZ, yaw, pitch);
            player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(1D, 0D, 1D)));
            world.playSound(null, to, ModSounds.TELEPORT, SoundSource.BLOCKS, 1F, 1F);
        });

        ctx.get().setPacketHandled(true);
    }

    private static int getPlayerExperienceProgress(Player player) {
        return Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static boolean validateTarget(BlockGetter world, BlockPos target) {
        return validateTarget(world.getBlockState(target.above(1))) && validateTarget(world.getBlockState(target.above(2)));
    }

    private static boolean validateTarget(BlockState blockState) {
        return !blockState.getMaterial().isSolid(); // TODO maybe change this
    }

    public static boolean isElevator(BlockState blockState) {
        return blockState.getBlock() instanceof ElevatorBlock;
    }
}
