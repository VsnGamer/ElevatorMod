package xyz.vsngamer.elevatorid.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
        ScreenManager.registerFactory(Registry.ELEVATOR_CONTAINER, ElevatorScreen::new);
        Minecraft.getInstance().getBlockColors().register(new ColorCamoElevator(), Registry.ELEVATOR_BLOCKS_ARRAY);

        Registry.ELEVATOR_BLOCKS.values().forEach(block ->
                RenderTypeLookup.setRenderLayer(block, renderType -> true));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {
        ModelLoader.addSpecialModel(new ResourceLocation("elevatorid", "arrow"));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
//        ELEVATOR_BLOCKS.values().forEach(elevatorBlock -> LogManager.getLogger().debug(elevatorBlock.getRegistryName()));

        // This is a little bit faster, still hacky
        e.getModelRegistry().keySet().forEach(key -> {
//            LogManager.getLogger().debug(key.getPath());
            if ("elevatorid".equals(key.getNamespace()) && key.getPath().contains("elevator_")) {
                IBakedModel originalModel = e.getModelRegistry().get(key);
                e.getModelRegistry().put(key, new ElevatorBakedModel(originalModel));
            }
        });

//        ELEVATOR_BLOCKS.values().forEach(block -> {
//            ResourceLocation regName = block.getRegistryName();
//            if (regName == null) return;
//
//            // SUPER HACKY
//            e.getModelRegistry().keySet().forEach(key -> {
//                if (key.getPath().equals(regName.getPath())) {
//                    IBakedModel originalModel = e.getModelRegistry().get(key);
//                    e.getModelRegistry().put(key, new ElevatorBakedModel(originalModel));
//                }
//            });
//        });
    }
}
