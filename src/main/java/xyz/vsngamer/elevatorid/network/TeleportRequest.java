package xyz.vsngamer.elevatorid.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

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

    static TeleportRequest decode(PacketBuffer buf) {
        return new TeleportRequest(buf.readBlockPos(), buf.readBlockPos());
    }

    static void encode(TeleportRequest msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.from);
        buf.writeBlockPos(msg.to);
    }
}
