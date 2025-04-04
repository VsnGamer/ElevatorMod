package com.vsngarcia.fabric;

import com.vsngarcia.Config;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.fabric.network.NetworkHandler;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public final class ElevatorModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ElevatorMod.init();
        FabricRegistry.init();
        NetworkHandler.init();

        ConfigRegistry.INSTANCE.register(ElevatorMod.ID, ModConfig.Type.SERVER, Config.SPEC);
        ModConfigEvents.reloading(ElevatorMod.ID).register(cfg -> ElevatorMod.LOGGER.info("Config reloaded"));
    }
}
