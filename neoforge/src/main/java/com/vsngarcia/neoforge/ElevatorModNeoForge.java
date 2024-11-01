package com.vsngarcia.neoforge;

import com.vsngarcia.Config;
import com.vsngarcia.ElevatorHandler;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.neoforge.init.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(ElevatorMod.ID)
public final class ElevatorModNeoForge {
    public ElevatorModNeoForge(IEventBus eventBus, ModContainer container) {
        ElevatorMod.init();

        Registry.init(eventBus);
        eventBus.addListener((ModConfigEvent.Reloading event) -> ElevatorMod.LOGGER.info("Config reloaded"));

        NeoForge.EVENT_BUS.addListener(
                (ClientTickEvent.Post e) -> ElevatorHandler.handleInput(PacketDistributor::sendToServer)
        );

        container.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }
}
