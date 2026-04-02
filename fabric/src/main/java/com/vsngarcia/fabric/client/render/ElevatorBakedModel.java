package com.vsngarcia.fabric.client.render;

import com.vsngarcia.fabric.ElevatorBlock;
import com.vsngarcia.fabric.client.ElevatorModFabricClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.function.Predicate;

import static com.vsngarcia.fabric.ElevatorBlock.DIRECTIONAL;
import static com.vsngarcia.fabric.ElevatorBlock.SHOW_ARROW;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

@Environment(EnvType.CLIENT)
public class ElevatorBakedModel extends WrapperBlockStateModel {
    private static final Minecraft MC = Minecraft.getInstance();

    public ElevatorBakedModel(BlockStateModel originalModel) {
        super(originalModel);
    }

    @Override
    public void emitQuads(
            QuadEmitter emitter,
            BlockAndTintGetter blockView,
            BlockPos pos,
            BlockState state,
            RandomSource random,
            Predicate<Direction> cullTest
    ) {
        if (!(state.getBlock() instanceof ElevatorBlock)) {
            super.emitQuads(emitter, blockView, pos, state, random, cullTest);
            return;
        }


        if (state.getValue(DIRECTIONAL) && state.getValue(SHOW_ARROW)) {
            emitter.pushTransform(quad -> {
                Vector3f vec = null;
                for (int i = 0; i < 4; i++) {
                    vec = quad.copyPos(i, vec);
                    vec.sub(.5f, 0, .5f);
                    vec.rotateY((float) Math.toRadians(-state.getValue(FACING).toYRot()));
                    vec.add(.5f, 0, .5f);

                    quad.pos(i, vec);
                }

                quad.chunkLayer(ChunkSectionLayer.CUTOUT);

                return true;
            });


            var arrow = MC.getModelManager().getModel(ElevatorModFabricClient.ElevatorModelLoadingPlugin.ARROW_MODEL_KEY);
            if (arrow != null) {
                arrow.emitQuads(emitter, blockView, pos, state, random, cullTest);
            }

            emitter.popTransform();
        }

        if (blockView.getBlockEntityRenderData(pos) instanceof BlockState heldState) {
            MC.getModelManager()
                    .getBlockStateModelSet()
                    .get(heldState)
                    .emitQuads(emitter, blockView, pos, heldState, random, cullTest);
            return;
        }

        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (level != BlockAndTintGetter.EMPTY
                && level.getBlockEntityRenderData(pos) instanceof BlockState heldState) {
            return MC.getModelManager()
                    .getBlockStateModelSet()
                    .get(heldState)
                    .particleMaterial(level, pos, heldState);
        }

        return super.particleMaterial(level, pos, state);
    }
}
