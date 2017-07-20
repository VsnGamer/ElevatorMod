package xyz.vsngamer.elevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TeleportRequest implements IMessage {
    private EnumFacing facing;

    public TeleportRequest() {
    }

    public TeleportRequest(EnumFacing facing) {
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.facing = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(facing.ordinal());
    }
}
