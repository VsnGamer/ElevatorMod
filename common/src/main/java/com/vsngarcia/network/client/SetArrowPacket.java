package com.vsngarcia.network.client;

import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.network.TeleportPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public record SetArrowPacket(boolean value, BlockPos pos) implements CustomPacketPayload {
    public static final Type<SetArrowPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(
            ElevatorMod.ID,
            "set_arrow"
    ));

    public static final StreamCodec<ByteBuf, SetArrowPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SetArrowPacket::value,
            BlockPos.STREAM_CODEC,
            SetArrowPacket::pos,
            SetArrowPacket::new
    );

    @Override
    public Type<SetArrowPacket> type() {
        return TYPE;
    }

    public static void handle(SetArrowPacket msg, ServerPlayer player) {
        if (TeleportPacket.isBadClientPacket(player, msg.pos)) {
            return;
        }

        Level world = player.level();
        BlockState state = world.getBlockState(msg.pos);
        if (state.getBlock() instanceof ElevatorBlockBase) {
            world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlockBase.SHOW_ARROW, msg.value));
        }
    }
}
