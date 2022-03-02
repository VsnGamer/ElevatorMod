package xyz.vsngamer.elevatorid.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
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

        Minecraft.getInstance().getBlockColors().register(
                new ColorCamoElevator(),
                Registry.ELEVATOR_BLOCKS.values().stream().map(RegistryObject::get).toArray(ElevatorBlock[]::new)
        );

        Registry.ELEVATOR_BLOCKS.values().forEach(o -> ItemBlockRenderTypes.setRenderLayer(o.get(), t -> true));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent e) {
        ForgeModelBakery.addSpecialModel(new ResourceLocation("elevatorid", "arrow"));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        e.getModelRegistry().entrySet().stream()
                .filter(entry -> "elevatorid".equals(entry.getKey().getNamespace()) && entry.getKey().getPath().contains("elevator_"))
                .forEach(entry -> e.getModelRegistry().put(entry.getKey(), new ElevatorBakedModel(entry.getValue())));
    }
}
