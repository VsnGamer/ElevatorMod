package com.vsngarcia.neoforge;

import com.vsngarcia.Config;
import com.vsngarcia.ElevatorHandler;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.neoforge.init.Registry;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ElevatorMod.ID)
public final class ElevatorModNeoForge {
    public ElevatorModNeoForge(IEventBus eventBus, ModContainer container) {
        ElevatorMod.init();

        Registry.init(eventBus);
        eventBus.addListener((ModConfigEvent.Reloading event) -> ElevatorMod.LOGGER.info("Config reloaded"));

        // Korrigiert: Verwende die Connection.send() Methode direkt
        // Da PacketDistributor.sendToServer() nicht verfügbar ist, verwenden wir die direkte Connection
        NeoForge.EVENT_BUS.addListener(
                (InputEvent.Key e) -> ElevatorHandler.handleInput(payload -> {
                    if (Minecraft.getInstance().getConnection() != null) {
                        Minecraft.getInstance().getConnection().send(payload);
                    }
                })
        );
        NeoForge.EVENT_BUS.addListener(
                (InputEvent.MouseButton.Post e) -> ElevatorHandler.handleInput(payload -> {
                    if (Minecraft.getInstance().getConnection() != null) {
                        Minecraft.getInstance().getConnection().send(payload);
                    }
                })
        );

        container.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }
}