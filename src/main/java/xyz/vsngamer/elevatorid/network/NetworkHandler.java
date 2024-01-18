package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;

public class NetworkHandler {
    private static final int PROTOCOL_VERSION = 1;
    public static SimpleChannel INSTANCE =
            ChannelBuilder.named(new ResourceLocation(ElevatorMod.ID, "main_channel"))
                    .acceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
                    .networkProtocolVersion(PROTOCOL_VERSION)
                    .simpleChannel();

    public static void init() {
        INSTANCE.messageBuilder(TeleportRequest.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(TeleportRequest::encode)
                .decoder(TeleportRequest::decode)
                .consumerMainThread(TeleportHandler::handle)
                .add();

        INSTANCE.messageBuilder(SetDirectionalPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetDirectionalPacket::encode)
                .decoder(SetDirectionalPacket::decode)
                .consumerMainThread(SetDirectionalPacket::handle)
                .add();

        INSTANCE.messageBuilder(SetArrowPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetArrowPacket::encode)
                .decoder(SetArrowPacket::decode)
                .consumerMainThread(SetArrowPacket::handle)
                .add();

        INSTANCE.messageBuilder(RemoveCamoPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RemoveCamoPacket::encode)
                .decoder(RemoveCamoPacket::decode)
                .consumerMainThread(RemoveCamoPacket::handle)
                .add();

        INSTANCE.messageBuilder(SetFacingPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetFacingPacket::encode)
                .decoder(SetFacingPacket::decode)
                .consumerMainThread(SetFacingPacket::handle)
                .add();
    }

    public static boolean isBadClientPacket(ServerPlayer player, BlockPos pos) {
        if (player == null || player.isDeadOrDying() || player.isRemoved()) return true;

        Level world = player.level();
        if (!world.isLoaded(pos)) return true;

        if (!(player.containerMenu instanceof ElevatorContainer container)) return true;

        return !container.getPos().equals(pos);
    }
}
