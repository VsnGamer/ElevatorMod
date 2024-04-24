package xyz.vsngamer.elevatorid.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

public record RemoveCamoPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<RemoveCamoPacket> TYPE = new Type<>(new ResourceLocation(ElevatorMod.ID, "remove_camo"));

    public static final StreamCodec<ByteBuf, RemoveCamoPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RemoveCamoPacket::pos,
            RemoveCamoPacket::new
    );

    @Override
    public Type<RemoveCamoPacket> type() {
        return TYPE;
    }

    public static void handle(RemoveCamoPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (NetworkHandler.isBadClientPacket(player, msg.pos)) return;

            if (player.level().getBlockEntity(msg.pos) instanceof ElevatorTileEntity tile) {
                tile.setCamoAndUpdate(null);
            }
        });
    }
}
