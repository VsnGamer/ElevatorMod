package com.vsngarcia.neoforge.network;

import com.vsngarcia.neoforge.init.Registry;
import com.vsngarcia.network.TeleportPacket;
import com.vsngarcia.network.client.RemoveCamoPacket;
import com.vsngarcia.network.client.SetArrowPacket;
import com.vsngarcia.network.client.SetDirectionalPacket;
import com.vsngarcia.network.client.SetFacingPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToServer(
                TeleportPacket.TYPE,
                TeleportPacket.STREAM_CODEC,
                (pkt, ctx) -> TeleportPacket.handle(pkt, (ServerPlayer) ctx.player(), Registry.TELEPORT_SOUND.get())
        );

        registrar.playToServer(
                SetDirectionalPacket.TYPE,
                SetDirectionalPacket.STREAM_CODEC,
                (pkt, ctx) -> SetDirectionalPacket.handle(pkt, (ServerPlayer) ctx.player())
        );
        registrar.playToServer(
                SetArrowPacket.TYPE,
                SetArrowPacket.STREAM_CODEC,
                (pkt, ctx) -> SetArrowPacket.handle(pkt, (ServerPlayer) ctx.player())
        );
        registrar.playToServer(
                RemoveCamoPacket.TYPE,
                RemoveCamoPacket.STREAM_CODEC,
                (pkt, ctx) -> RemoveCamoPacket.handle(pkt, (ServerPlayer) ctx.player())
        );
        registrar.playToServer(
                SetFacingPacket.TYPE,
                SetFacingPacket.STREAM_CODEC,
                (pkt, ctx) -> SetFacingPacket.handle(pkt, (ServerPlayer) ctx.player())
        );
    }
}
