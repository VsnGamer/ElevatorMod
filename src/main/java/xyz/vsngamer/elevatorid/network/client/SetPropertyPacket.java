package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.network.Packet;

import java.util.function.Supplier;

public class SetPropertyPacket implements Packet {

    @Override
    public void encode(Packet msg, PacketBuffer buf) {

    }

    @Override
    public Packet decode(PacketBuffer buf) {
        return null;
    }

    @Override
    public void handle(Packet msg, Supplier<NetworkEvent.Context> ctx) {

    }
}
