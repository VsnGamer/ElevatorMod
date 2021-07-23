package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class TeleportRequest {
    private final BlockPos from, to;

    public TeleportRequest(BlockPos from, BlockPos to) {
        this.from = from;
        this.to = to;
    }

    BlockPos getFrom() {
        return from;
    }

    BlockPos getTo() {
        return to;
    }

    static TeleportRequest decode(FriendlyByteBuf buf) {
        return new TeleportRequest(buf.readBlockPos(), buf.readBlockPos());
    }

    static void encode(TeleportRequest msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.from);
        buf.writeBlockPos(msg.to);
    }
}
