package xyz.vsngamer.elevatorid.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet {
    void encode(Packet msg, PacketBuffer buf);

    Packet decode(PacketBuffer buf);

    void handle(Packet msg, Supplier<NetworkEvent.Context> ctx);
}
