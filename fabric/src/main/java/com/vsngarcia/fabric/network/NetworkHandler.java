package com.vsngarcia.fabric.network;

import com.vsngarcia.fabric.FabricRegistry;
import com.vsngarcia.network.TeleportPacket;
import com.vsngarcia.network.client.RemoveCamoPacket;
import com.vsngarcia.network.client.SetArrowPacket;
import com.vsngarcia.network.client.SetDirectionalPacket;
import com.vsngarcia.network.client.SetFacingPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public class NetworkHandler {


    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(TeleportPacket.TYPE, TeleportPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                TeleportPacket.TYPE,
                (pkt, ctx) -> TeleportPacket.handle(pkt, ctx.player(), FabricRegistry.TELEPORT_SOUND)
        );

        PayloadTypeRegistry.serverboundPlay().register(SetDirectionalPacket.TYPE, SetDirectionalPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                SetDirectionalPacket.TYPE,
                (pkt, ctx) -> SetDirectionalPacket.handle(pkt, ctx.player())
        );

        PayloadTypeRegistry.serverboundPlay().register(SetArrowPacket.TYPE, SetArrowPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                SetArrowPacket.TYPE,
                (pkt, ctx) -> SetArrowPacket.handle(pkt, ctx.player())
        );

        PayloadTypeRegistry.serverboundPlay().register(RemoveCamoPacket.TYPE, RemoveCamoPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                RemoveCamoPacket.TYPE,
                (pkt, ctx) -> RemoveCamoPacket.handle(pkt, ctx.player())
        );

        PayloadTypeRegistry.serverboundPlay().register(SetFacingPacket.TYPE, SetFacingPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                SetFacingPacket.TYPE,
                (pkt, ctx) -> SetFacingPacket.handle(pkt, ctx.player())
        );
    }
}
