package xyz.vsngamer.elevatorid.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import xyz.vsngamer.elevatorid.ElevatorMod;

public record TeleportRequest(BlockPos from, BlockPos to) implements CustomPacketPayload {
    public static final Type<TeleportRequest> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "teleport_request"));

    public static final StreamCodec<ByteBuf, TeleportRequest> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            TeleportRequest::from,
            BlockPos.STREAM_CODEC,
            TeleportRequest::to,
            TeleportRequest::new
    );

    @Override
    public Type<TeleportRequest> type() {
        return TYPE;
    }
}
