package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ElevatorMod.ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(i++, TeleportRequest.class, TeleportRequest::encode, TeleportRequest::decode, TeleportHandler::handle);
        INSTANCE.registerMessage(i++, SetDirectionalPacket.class, SetDirectionalPacket::encode, SetDirectionalPacket::decode, SetDirectionalPacket::handle);
        INSTANCE.registerMessage(i++, SetArrowPacket.class, SetArrowPacket::encode, SetArrowPacket::decode, SetArrowPacket::handle);
        INSTANCE.registerMessage(i++, RemoveCamoPacket.class, RemoveCamoPacket::encode, RemoveCamoPacket::decode, RemoveCamoPacket::handle);
        INSTANCE.registerMessage(i++, SetFacingPacket.class, SetFacingPacket::encode, SetFacingPacket::decode, SetFacingPacket::handle);
    }

    public static boolean isBadClientPacket(ServerPlayer player, BlockPos pos) {
        if (player == null || player.isDeadOrDying() || player.isRemoved())
            return true;

        ServerLevel world = player.getLevel();
        if (!world.isLoaded(pos))
            return true;

        if (!(player.containerMenu instanceof ElevatorContainer container))
            return true;

        return !container.getPos().equals(pos);
    }
}
