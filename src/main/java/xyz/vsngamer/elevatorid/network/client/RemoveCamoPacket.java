package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.function.Supplier;

public record RemoveCamoPacket(BlockPos pos) {
    public static void encode(RemoveCamoPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RemoveCamoPacket decode(FriendlyByteBuf buf) {
        return new RemoveCamoPacket(buf.readBlockPos());
    }

    public static void handle(RemoveCamoPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (NetworkHandler.isBadClientPacket(player, msg.pos))
                return;

            if (player.getLevel().getBlockEntity(msg.pos) instanceof ElevatorTileEntity tile) {
                tile.setCamoAndUpdate(null);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
