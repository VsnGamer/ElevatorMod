package com.vsngarcia.network.client;

import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.network.TeleportPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public record SetFacingPacket(Direction direction, BlockPos pos) implements CustomPacketPayload {
    public static final Type<SetFacingPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(
            ElevatorMod.ID,
            "set_facing"
    ));

    public static final StreamCodec<ByteBuf, SetFacingPacket> STREAM_CODEC = StreamCodec.composite(
            Direction.STREAM_CODEC,
            SetFacingPacket::direction,
            BlockPos.STREAM_CODEC,
            SetFacingPacket::pos,
            SetFacingPacket::new
    );

    @Override
    public Type<SetFacingPacket> type() {
        return TYPE;
    }

    public static void handle(SetFacingPacket msg, ServerPlayer player) {
        if (TeleportPacket.isBadClientPacket(player, msg.pos)) {
            return;
        }

        Level world = player.level();
        BlockState state = world.getBlockState(msg.pos);
        if (state.getBlock() instanceof ElevatorBlockBase) {
            world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlockBase.FACING, msg.direction));
        }
    }
}
