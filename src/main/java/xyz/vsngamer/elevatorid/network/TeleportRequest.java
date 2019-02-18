package xyz.vsngamer.elevatorid.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class TeleportRequest {
    private final BlockPos from, to;

    //public TeleportRequest() {}

    public TeleportRequest(BlockPos from, BlockPos to) {
        this.from = from;
        this.to = to;
    }

    public BlockPos getFrom() {
        return from;
    }

    public BlockPos getTo() {
        return to;
    }

    static TeleportRequest decode(PacketBuffer buf) {
        return new TeleportRequest(buf.readBlockPos(), buf.readBlockPos());

//        from = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
//        to = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    static void encode(TeleportRequest msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.from);
        buf.writeBlockPos(msg.to);

//        buf.writeInt(from.getX());
//        buf.writeInt(from.getY());
//        buf.writeInt(from.getZ());
//
//        buf.writeInt(to.getX());
//        buf.writeInt(to.getY());
//        buf.writeInt(to.getZ());
    }
}
