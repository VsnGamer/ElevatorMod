package xyz.vsngamer.elevatorid.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.vsngamer.elevatorid.ElevatorMod;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel networkHandler = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ElevatorMod.ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void init() {
        networkHandler.registerMessage(0, TeleportRequest.class, TeleportRequest::encode, TeleportRequest::decode, TeleportHandler::handle);
    }
}
