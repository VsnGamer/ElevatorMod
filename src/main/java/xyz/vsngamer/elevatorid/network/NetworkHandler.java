package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToServer(TeleportRequest.TYPE, TeleportRequest.STREAM_CODEC, TeleportHandler::handle);
        registrar.playToServer(SetDirectionalPacket.TYPE, SetDirectionalPacket.STREAM_CODEC, SetDirectionalPacket::handle);
        registrar.playToServer(SetArrowPacket.TYPE, SetArrowPacket.STREAM_CODEC, SetArrowPacket::handle);
        registrar.playToServer(RemoveCamoPacket.TYPE, RemoveCamoPacket.STREAM_CODEC, RemoveCamoPacket::handle);
        registrar.playToServer(SetFacingPacket.TYPE, SetFacingPacket.STREAM_CODEC, SetFacingPacket::handle);
    }

    public static boolean isBadClientPacket(Player player, BlockPos pos) {
        if (player == null || player.isDeadOrDying() || player.isRemoved())
            return true;

        Level world = player.level();
        if (!world.isLoaded(pos))
            return true;

        if (!(player.containerMenu instanceof ElevatorContainer container))
            return true;

        return !container.getPos().equals(pos);
    }
}
