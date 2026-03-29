package com.vsngarcia.neoforge.client;

import com.vsngarcia.ElevatorMod;
import com.vsngarcia.client.ColorCamoElevator;
import com.vsngarcia.client.gui.ElevatorScreen;
import com.vsngarcia.level.ElevatorContainer;
import com.vsngarcia.neoforge.ElevatorBlock;
import com.vsngarcia.neoforge.client.render.ElevatorBakedModel;
import com.vsngarcia.neoforge.init.Registry;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Rotation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@EventBusSubscriber(modid = ElevatorMod.ID, value = Dist.CLIENT)
public class ClientRegistry {
    public static final Map<Direction, StandaloneModelKey<BlockStateModel>> ARROW_MODEL_KEYS =
            Direction.Plane.HORIZONTAL.stream().collect(Collectors.toUnmodifiableMap(
                    Function.identity(),
                    dir -> new StandaloneModelKey<>(() -> "arrow_" + dir.getSerializedName())
            ));

    @SubscribeEvent
    public static void onMenuScreensRegistry(RegisterMenuScreensEvent e) {
        e.register(Registry.ELEVATOR_CONTAINER.get(), (ElevatorContainer container, Inventory inv, Component title) -> new ElevatorScreen(container, inv, title, ClientPacketDistributor::sendToServer));
    }

    @SubscribeEvent
    public static void onBlockColorHandlersRegistry(RegisterColorHandlersEvent.BlockTintSources e) {
        e.register(Collections.singletonList(new ColorCamoElevator()), Registry.ELEVATOR_BLOCKS.values().stream().map(DeferredHolder::get).toArray(ElevatorBlock[]::new));
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelEvent.RegisterStandalone e) {
        // HACK: Pre-bake all rotations
        Direction.Plane.HORIZONTAL.forEach(dir ->
                e.register(
                        ARROW_MODEL_KEYS.get(dir),
                        SimpleUnbakedStandaloneModel.blockStateModel(
                                Identifier.fromNamespaceAndPath(ElevatorMod.ID, "arrow"),
                                BlockModelRotation.get(Rotation.values()[(int) dir.toYRot() / 90].rotation())
                        )
                )
        );
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult e) {
        e.getBakingResult()
                .blockStateModels()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getBlock() instanceof ElevatorBlock)
                .forEach(
                        entry -> e.getBakingResult()
                                .blockStateModels()
                                .put(entry.getKey(), new ElevatorBakedModel(entry.getValue()))
                );
    }
}
