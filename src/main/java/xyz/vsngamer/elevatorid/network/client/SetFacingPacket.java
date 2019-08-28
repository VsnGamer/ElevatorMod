package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetFacingPacket {


    private final Direction direction;
    private final BlockPos pos;

    public <T> SetFacingPacket(T value, BlockPos pos) {
        this.direction = value;
        this.pos = pos;
    }

    public static <MSG> void encode(MSG msg, PacketBuffer packetBuffer) {
    }

    public static <MSG> MSG decode(PacketBuffer packetBuffer) {
    }

    public static <MSG> void handle(MSG msg, Supplier<NetworkEvent.Context> contextSupplier) {
    }
}
