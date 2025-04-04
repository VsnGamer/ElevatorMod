package com.vsngarcia.neoforge.client.render;

import com.vsngarcia.neoforge.ElevatorBlock;
import com.vsngarcia.neoforge.client.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.List;


public class ElevatorBakedModel extends DelegateBlockStateModel {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(BlockStateModel originalModel) {
        super(originalModel);
    }


    @Nonnull
    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState elevator) {
        BlockState state = level.getModelData(pos).get(HELD_STATE);
        if (state != null) {
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).particleIcon(level, pos, state);
        }

        return super.particleIcon(level, pos, elevator);
    }


    @Nonnull
    @Override
    public void collectParts(
            BlockAndTintGetter level,
            BlockPos pos,
            BlockState state,
            RandomSource random,
            List<BlockModelPart> parts
    ) {
        // Directional arrow
        if (state.getValue(ElevatorBlock.DIRECTIONAL) && state.getValue(ElevatorBlock.SHOW_ARROW)) {
            var arrowModels = Minecraft.getInstance()
                    .getModelManager()
                    .getStandaloneModel(ClientRegistry.ARROW_MODEL_KEY);

            if (arrowModels != null) {
                arrowModels.get(state.getValue(ElevatorBlock.FACING)).collectParts(level, pos, state, random, parts);
            }
        }


        BlockState heldState = level.getModelData(pos).get(HELD_STATE);
        if (heldState != null) {
            BlockStateModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(heldState);

            blockModel.collectParts(level, pos, heldState, random, parts);

            return;
        }

        // Fallback / original model
        super.collectParts(level, pos, state, random, parts);
    }
}
