package com.vsngarcia.fabric.client;

import com.vsngarcia.ElevatorHandler;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.client.ColorCamoElevator;
import com.vsngarcia.client.gui.ElevatorScreen;
import com.vsngarcia.fabric.ElevatorBlock;
import com.vsngarcia.fabric.client.render.ElevatorBakedModel;
import com.vsngarcia.level.ElevatorContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.vsngarcia.fabric.FabricRegistry.ELEVATOR_BLOCKS;
import static com.vsngarcia.fabric.FabricRegistry.ELEVATOR_CONTAINER;

@Environment(EnvType.CLIENT)
public final class ElevatorModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> ElevatorHandler.handleInput(ClientPlayNetworking::send));

        ColorProviderRegistry.BLOCK.register(
                new ColorCamoElevator(),
                ELEVATOR_BLOCKS.values().toArray(new Block[0])
        );
        ModelLoadingPlugin.register(new ElevatorModelLoadingPlugin());

        MenuScreens.register(
                ELEVATOR_CONTAINER,
                (ElevatorContainer container, Inventory inv, Component title) ->
                        new ElevatorScreen(container, inv, title, ClientPlayNetworking::send)
        );
    }

    public static class ElevatorModelLoadingPlugin implements ModelLoadingPlugin {
        @Override
        public void initialize(Context ctx) {
            ctx.modifyBlockModelOnLoad().register(
                    ModelModifier.WRAP_PHASE,
                    (model, context) -> {
                        if (!(context.state().getBlock() instanceof ElevatorBlock)) {
                            return model;
                        }

                        ElevatorMod.LOGGER.debug("Wrapping elevator model: {}", context.state());
                        return new WrapperUnbakedGroupedBlockStateModel(model) {
                            @Override
                            public BlockStateModel bake(BlockState state, ModelBaker baker) {
                                return new ElevatorBakedModel(model.bake(context.state(),baker));
                            }
                        };
                    }
            );


//            ctx.addModels(ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "arrow"));
        }
    }
}
