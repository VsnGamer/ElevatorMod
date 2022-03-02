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
import net.minecraftforge.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.init.ModSounds;

import java.util.Arrays;
import java.util.function.Supplier;

public class TeleportHandler {
    static void handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (isBadTeleportPacket(message, player))
                return;

            // XP
            if (ModConfig.GENERAL.useXP.get() && !player.isCreative()) {
                Integer xpCost = ModConfig.GENERAL.XPPointsAmount.get();
                if (getPlayerExperienceProgress(player) - xpCost >= 0 || player.experienceLevel > 0) {
                    player.giveExperiencePoints(-xpCost);
                } else {
                    player.sendMessage(
                            new TranslatableComponent("elevatorid.message.missing_xp")
                                    .withStyle(ChatFormatting.RED), player.getUUID()
                    );
                    return;
                }
            }

            ServerLevel world = player.getLevel();
            BlockPos toPos = message.getTo();
            BlockState toState = world.getBlockState(message.getTo());

            // Check yaw and pitch
            final float yaw, pitch;
            yaw = toState.getValue(ElevatorBlock.DIRECTIONAL)
                    ? toState.getValue(ElevatorBlock.FACING).toYRot() : player.getYRot();

            pitch = (toState.getValue(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchDirectional.get())
                    || (!toState.getValue(ElevatorBlock.DIRECTIONAL) && ModConfig.GENERAL.resetPitchNormal.get())
                    ? 0F : player.getXRot();

            // Check X and Z
            final double toX, toZ;
            if (ModConfig.GENERAL.precisionTarget.get()) {
                toX = toPos.getX() + .5D;
                toZ = toPos.getZ() + .5D;
            } else {
                toX = player.getX();
                toZ = player.getZ();
            }

            double blockYOffset = toState.getBlockSupportShape(world, toPos).max(Direction.Axis.Y);
            player.teleportTo(world, toX, Math.max(toPos.getY(), toPos.getY() + blockYOffset), toZ, yaw, pitch);
            player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(1D, 0D, 1D)));
            world.playSound(null, toPos, ModSounds.TELEPORT, SoundSource.BLOCKS, 1F, 1F);
        });

        ctx.get().setPacketHandled(true);
    }

    private static boolean isBadTeleportPacket(TeleportRequest msg, ServerPlayer player) {
        if (player == null || !player.isAlive())
            return true;

        ServerLevel world = player.getLevel();
        BlockPos fromPos = msg.getFrom();
        BlockPos toPos = msg.getTo();

        if (!world.isLoaded(fromPos) || !world.isLoaded(toPos))
            return true;

        // This ensures the player is still standing on the "from" elevator
        final double distanceSq = player.distanceToSqr(Vec3.atCenterOf(fromPos.north()));
        if (distanceSq > 4D)
            return true;

        if (fromPos.getX() != toPos.getX() || fromPos.getZ() != toPos.getZ())
            return true;

        ElevatorBlock fromElevator = getElevator(world.getBlockState(fromPos));
        ElevatorBlock toElevator = getElevator(world.getBlockState(toPos));

        if (fromElevator == null || toElevator == null)
            return true;

        if (!isBlocked(world, toPos))
            return true;

        return ModConfig.GENERAL.sameColor.get() && fromElevator.getColor() != toElevator.getColor();
    }

    private static int getPlayerExperienceProgress(Player player) {
        return Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static boolean isBlocked(BlockGetter world, BlockPos target) {
        return validateTargets(world.getBlockState(target.above()), world.getBlockState(target.above(2)));
    }

    private static boolean validTarget(BlockState blockState) {
        return !blockState.getMaterial().isSolid(); // TODO maybe change this
    }

    private static boolean validateTargets(BlockState... states) {
        return Arrays.stream(states).allMatch(TeleportHandler::validTarget);
    }

    public static ElevatorBlock getElevator(BlockState blockState) {
        if (blockState.getBlock() instanceof ElevatorBlock elevator)
            return elevator;
        return null;
    }
}
