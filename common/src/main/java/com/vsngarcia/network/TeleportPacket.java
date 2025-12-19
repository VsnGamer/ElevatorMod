package com.vsngarcia.network;

import com.vsngarcia.Config;
import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.level.ElevatorContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public record TeleportPacket(BlockPos from, BlockPos to) implements CustomPacketPayload {
    public static final Type<TeleportPacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(ElevatorMod.ID, "teleport_request")
    );

    public static final StreamCodec<ByteBuf, TeleportPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            TeleportPacket::from,
            BlockPos.STREAM_CODEC,
            TeleportPacket::to,
            TeleportPacket::new
    );

    @Override
    public Type<TeleportPacket> type() {
        return TYPE;
    }

    public static void handle(TeleportPacket message, ServerPlayer player, SoundEvent soundEvent) {
        if (isBadTeleportPacket(message, player)) {
            return;
        }

        // XP
        if (Config.GENERAL.useXP.get() && !player.isCreative()) {
            Integer xpCost = Config.GENERAL.XPPointsAmount.get();
            if (getPlayerExperienceProgress(player) - xpCost >= 0 || player.experienceLevel > 0) {
                player.giveExperiencePoints(-xpCost);
            } else {
                player.displayClientMessage(
                        Component.translatable("elevatorid.message.missing_xp").withStyle(ChatFormatting.RED),
                        true
                );
                return;
            }
        }

        ServerLevel world = player.level();

        BlockPos toPos = message.to();
        BlockState toState = world.getBlockState(message.to());

        // Check X and Z
        double toX, toZ;
        if (Config.GENERAL.precisionTarget.get()) {
            toX = toPos.getX() + .5D;
            toZ = toPos.getZ() + .5D;
        } else {
            toX = player.getX();
            toZ = player.getZ();
        }

        double blockYOffset = toState.getBlockSupportShape(world, toPos).max(Direction.Axis.Y);
        double toY = Math.max(toPos.getY(), toPos.getY() + blockYOffset);

        // Check yaw and pitch
        var directional = toState.getValue(ElevatorBlockBase.DIRECTIONAL);
        var resetPitch = directional ? Config.GENERAL.resetPitchDirectional.get() : Config.GENERAL.resetPitchNormal.get();

        var relative = EnumSet.noneOf(Relative.class);
        if (!directional) {
            relative.add(Relative.Y_ROT);
        }

        if (!resetPitch) {
            relative.add(Relative.X_ROT);
        }

        player.teleportTo(
                world,
                toX,
                toY,
                toZ,
                relative,
                !directional ? 0 : toState.getValue(ElevatorBlockBase.FACING).toYRot(),
                0,
                true
        );

        player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(1D, 0D, 1D)));

        world.playSound(null, toPos, soundEvent, SoundSource.BLOCKS, 1F, 1F);
    }

    private static boolean isBadTeleportPacket(TeleportPacket msg, Player player) {
        if (player == null || !player.isAlive())
            return true;

        Level world = player.level();
        BlockPos fromPos = msg.from();
        BlockPos toPos = msg.to();

        if (!world.isLoaded(fromPos) || !world.isLoaded(toPos))
            return true;

        // This ensures the player is still standing on the "from" elevator
        if (player.blockPosition().distManhattan(fromPos) > Config.GENERAL.activationRange.getAsInt())
            return true;

        if (fromPos.getX() != toPos.getX() || fromPos.getZ() != toPos.getZ())
            return true;

        if (fromPos.getY() == toPos.getY())
            return true;

        ElevatorBlockBase fromElevator = getElevator(world.getBlockState(fromPos));
        ElevatorBlockBase toElevator = getElevator(world.getBlockState(toPos));

        if (fromElevator == null || toElevator == null)
            return true;

        if (!isValidPos(world, toPos))
            return true;

        return Config.GENERAL.sameColor.get() && fromElevator.getColor() != toElevator.getColor();
    }

    private static int getPlayerExperienceProgress(Player player) {
        return Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static boolean isValidPos(BlockGetter world, BlockPos pos) {
        return !world.getBlockState(pos.above()).isSuffocating(world, pos.above());
    }

    public static ElevatorBlockBase getElevator(BlockState blockState) {
        if (blockState.getBlock() instanceof ElevatorBlockBase elevator) {
            return elevator;
        }

        return null;
    }

    public static boolean isBadClientPacket(Player player, BlockPos pos) {
        if (player == null || player.isDeadOrDying() || player.isRemoved())
            return true;

        Level world = player.level();
        if (!world.isLoaded(pos))
            return true;

        if (!(player.containerMenu instanceof ElevatorContainer container))
            return true;

        return !container.getPos().equals(pos);
    }
}
