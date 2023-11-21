package xyz.vsngamer.elevatorid;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import xyz.vsngamer.elevatorid.init.Registry;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

@Mod(ElevatorMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElevatorMod {
    public static final String ID = "elevatorid";

    public ElevatorMod(IEventBus eventBus) {
        Registry.init(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, xyz.vsngamer.elevatorid.init.ModConfig.SPEC);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.init();
    }
}
