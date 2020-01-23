package xyz.vsngamer.elevatorid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xyz.vsngamer.elevatorid.client.gui.ElevatorScreen;
import xyz.vsngamer.elevatorid.client.render.ColorCamoElevator;
import xyz.vsngamer.elevatorid.init.Registry;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

@Mod(ElevatorMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElevatorMod {

    public static final String ID = "elevatorid";

    public ElevatorMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, xyz.vsngamer.elevatorid.init.ModConfig.SPEC);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.init();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registry.ELEVATOR_CONTAINER, ElevatorScreen::new);
        Minecraft.getInstance().getBlockColors().register(new ColorCamoElevator(), Registry.ELEVATOR_BLOCKS_ARRAY);

        // TODO RENDER LAYERS
        Registry.ELEVATOR_BLOCKS.values().forEach(block -> {
            RenderTypeLookup.setRenderLayer(block, renderType -> renderType == RenderType.translucent() || renderType == RenderType.cutoutMipped());
//            RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
        });
    }
}
