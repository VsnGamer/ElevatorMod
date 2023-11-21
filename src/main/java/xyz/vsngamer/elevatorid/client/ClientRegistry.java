package xyz.vsngamer.elevatorid.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.client.gui.ElevatorScreen;
import xyz.vsngamer.elevatorid.client.render.ColorCamoElevator;
import xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(Registry.ELEVATOR_CONTAINER.get(), ElevatorScreen::new);
    }

    @SubscribeEvent
    public static void onBlockColorHandlersRegistry(RegisterColorHandlersEvent.Block e) {
        e.register(
                new ColorCamoElevator(),
                Registry.ELEVATOR_BLOCKS.values().stream().map(DeferredHolder::get).toArray(ElevatorBlock[]::new)
        );
    }


    @SubscribeEvent
    public static void onModelRegistry(ModelEvent.RegisterAdditional e) {
        e.register(new ResourceLocation("elevatorid", "arrow"));
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult e) {
        e.getModels().entrySet().stream()
                .filter(entry -> "elevatorid".equals(entry.getKey().getNamespace()) && entry.getKey().getPath().contains("elevator_"))
                .forEach(entry -> e.getModels().put(entry.getKey(), new ElevatorBakedModel(entry.getValue())));
    }
}
