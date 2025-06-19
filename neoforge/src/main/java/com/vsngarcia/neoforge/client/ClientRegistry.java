package com.vsngarcia.neoforge.client;

import com.mojang.math.Quadrant;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.client.ColorCamoElevator;
import com.vsngarcia.client.gui.ElevatorScreen;
import com.vsngarcia.level.ElevatorContainer;
import com.vsngarcia.neoforge.ElevatorBlock;
import com.vsngarcia.neoforge.client.render.ElevatorBakedModel;
import com.vsngarcia.neoforge.init.Registry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;


@EventBusSubscriber(modid = ElevatorMod.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {

    public static final StandaloneModelKey<EnumMap<Direction, SingleVariant>> ARROW_MODEL_KEY = new StandaloneModelKey<>(ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "arrow"));

    @SubscribeEvent
    public static void onMenuScreensRegistry(RegisterMenuScreensEvent e) {
        e.register(Registry.ELEVATOR_CONTAINER.get(), (ElevatorContainer container, Inventory inv, Component title) -> new ElevatorScreen(container, inv, title, PacketDistributor::sendToServer));
    }

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        // HACK: As far as I was able to understand, vanilla blocks use a specific logic for determining render types
        //  I think that for now it can't detect mimics getAppearance
        //  For now we just use TRANSLUCENT, which is not ideal but covers most cases
        Registry.ELEVATOR_BLOCKS.values().forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.get(), ChunkSectionLayer.TRANSLUCENT));
    }


    @SubscribeEvent
    public static void onBlockColorHandlersRegistry(RegisterColorHandlersEvent.Block e) {
        e.register(new ColorCamoElevator(), Registry.ELEVATOR_BLOCKS.values().stream().map(DeferredHolder::get).toArray(ElevatorBlock[]::new));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelEvent.RegisterStandalone e) {
        // HACK: Pre-bake all rotations
        e.register(
                ARROW_MODEL_KEY,
                (model, baker) -> Direction.Plane.HORIZONTAL
                        .stream()
                        .collect(Collectors.toMap(
                                Function.identity(), d -> new SingleVariant(
                                        SimpleModelWrapper.bake(
                                                baker,
                                                model,
                                                BlockModelRotation.by(Quadrant.R0, Quadrant.parseJson((int) d.toYRot()))
                                        )
                                ),
                                (o1, o2) -> o1,
                                () -> new EnumMap<>(Direction.class)
                        ))
        );
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult e) {
        e.getBakingResult().blockStateModels().entrySet().stream().filter(entry -> entry.getKey().getBlock() instanceof ElevatorBlock).forEach(entry -> e.getBakingResult().blockStateModels().put(entry.getKey(), new ElevatorBakedModel(entry.getValue())));
    }
}
