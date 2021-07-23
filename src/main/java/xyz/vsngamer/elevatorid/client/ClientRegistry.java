package xyz.vsngamer.elevatorid.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.client.gui.ElevatorScreen;
import xyz.vsngamer.elevatorid.client.render.ColorCamoElevator;
import xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(Registry.ELEVATOR_CONTAINER, ElevatorScreen::new);
        Minecraft.getInstance().getBlockColors().register(new ColorCamoElevator(), Registry.ELEVATOR_BLOCKS_ARRAY);

        Registry.ELEVATOR_BLOCKS.values().forEach(block ->
                ItemBlockRenderTypes.setRenderLayer(block, renderType -> true));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {
        ModelLoader.addSpecialModel(new ResourceLocation("elevatorid", "arrow"));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        // This is a little bit faster, still hacky
        e.getModelRegistry().entrySet().stream()
                .filter(entry -> "elevatorid".equals(entry.getKey().getNamespace()) && entry.getKey().getPath().contains("elevator_"))
                .forEach(entry -> {
                    BakedModel originalModel = entry.getValue();
                    e.getModelRegistry().put(entry.getKey(), new ElevatorBakedModel(originalModel));
                });
    }
}
