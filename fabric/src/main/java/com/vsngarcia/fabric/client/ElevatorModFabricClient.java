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
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
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

        ColorProviderRegistry.BLOCK.register(new ColorCamoElevator(), ELEVATOR_BLOCKS.values().toArray(new Block[0]));
        ModelLoadingPlugin.register(new ElevatorModelLoadingPlugin());

        MenuScreens.register(
                ELEVATOR_CONTAINER,
                (ElevatorContainer container, Inventory inv, Component title) -> new ElevatorScreen(
                        container,
                        inv,
                        title,
                        ClientPlayNetworking::send
                )
        );
    }

    public static class ElevatorModelLoadingPlugin implements ModelLoadingPlugin {

        @Override
        public void initialize(Context ctx) {
            ctx.modifyBlockModelOnLoad().register(
                    ModelModifier.WRAP_PHASE, (model, context) -> {
                        if (!(context.state().getBlock() instanceof ElevatorBlock)) {
                            return model;
                        }

                        ElevatorMod.LOGGER.debug("Wrapping elevator model: {}", context.state());
                        return new WrapperUnbakedGroupedBlockStateModel(model) {
                            @Override
                            public BlockStateModel bake(BlockState state, ModelBaker baker) {
                                return new ElevatorBakedModel(model.bake(context.state(), baker), getArrowMesh(baker));
                            }
                        };
                    }
            );

//            ctx.addModels(ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "arrow"));
        }

        private static final Material ARROW_SPRITE = new Material(
                TextureAtlas.LOCATION_BLOCKS,
                ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "block/arrow")
        );

        private Mesh arrowMesh = null;

        // HACK: Waiting for extra models support https://github.com/FabricMC/fabric/pull/4565
        private Mesh getArrowMesh(ModelBaker baker) {
            if (arrowMesh != null) {
                return arrowMesh;
            }

            TextureAtlasSprite atlasSprite = baker.sprites().get(ARROW_SPRITE, () -> "elevator_arrow");

            Renderer renderer = Renderer.get();
            MutableMesh builder = renderer.mutableMesh();
            QuadEmitter emitter = builder.emitter();

            emitter.square(Direction.UP, 0.3125F, 0, 0.6875F, 0.375F, -.01F);
            emitter.uvUnitSquare();
            emitter.spriteBake(atlasSprite, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.cullFace(Direction.UP);
            emitter.material(renderer.materialFinder().blendMode(BlendMode.CUTOUT_MIPPED).find());
            emitter.emit();

            this.arrowMesh = builder.immutableCopy();
            return arrowMesh;
        }
    }
}
