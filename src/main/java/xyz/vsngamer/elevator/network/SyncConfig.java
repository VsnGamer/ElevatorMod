package xyz.vsngamer.elevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncConfig implements IMessage {

    private boolean sameColor;
    private int range;
    private boolean skipUnreachable;

    public SyncConfig() {}

    public SyncConfig(boolean sameColor, int  range, boolean skipUnreachable){
        this.sameColor = sameColor;
        this.range = range;
        this.skipUnreachable = skipUnreachable;
    }

    boolean getSameColor() {
        return sameColor;
    }

    int getRange() {
        return range;
    }
    
    boolean getSkipUnreachable() {
        return skipUnreachable;
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
