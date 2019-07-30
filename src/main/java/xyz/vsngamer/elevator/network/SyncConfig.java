package xyz.vsngamer.elevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncConfig implements IMessage {

    private boolean sameColor;
    private int range;

    public SyncConfig() {}

    public SyncConfig(boolean sameColor, int  range){
        this.sameColor = sameColor;
        this.range = range;
    }

    boolean getSameColor() {
        return sameColor;
    }

    int getRange() {
        return range;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sameColor = buf.readBoolean();
        range = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(sameColor);
        buf.writeInt(range);
    }
}
