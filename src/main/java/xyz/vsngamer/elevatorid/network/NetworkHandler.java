package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;

@Mod(ElevatorMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(ElevatorMod.ID);

        registrar.play(TeleportRequest.ID, TeleportRequest::new, handler -> handler.server(TeleportHandler.getInstance()::handle));
        registrar.play(SetDirectionalPacket.ID, SetDirectionalPacket::new, handler -> handler.server(SetDirectionalPacket.Handler.getInstance()::handle));
        registrar.play(SetArrowPacket.ID, SetArrowPacket::new, handler -> handler.server(SetArrowPacket.Handler.getInstance()::handle));
        registrar.play(RemoveCamoPacket.ID, RemoveCamoPacket::new, handler -> handler.server(RemoveCamoPacket.Handler.getInstance()::handle));
        registrar.play(SetFacingPacket.ID, SetFacingPacket::new, handler -> handler.server(SetFacingPacket.Handler.getInstance()::handle));
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
