package com.vsngarcia.neoforge.client.render;

import com.vsngarcia.neoforge.ElevatorBlock;
import com.vsngarcia.neoforge.client.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelProperty;

import java.util.List;


public class ElevatorBakedModel extends DelegateBlockStateModel {
    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(BlockStateModel originalModel) {
        super(originalModel);
    }


    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        BlockState heldState = level.getModelData(pos).get(HELD_STATE);

        if (heldState != null) {
            return Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(heldState).particleMaterial(level, pos, heldState);
        }

        return super.particleMaterial(level, pos, state);
    }


    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        if (!(state.getBlock() instanceof ElevatorBlock)) {
            super.collectParts(level, pos, state, random, parts);
            return;
        }

        // Directional arrow
        if (state.getValue(ElevatorBlock.DIRECTIONAL) && state.getValue(ElevatorBlock.SHOW_ARROW)) {
            var arrowModel = Minecraft.getInstance()
                    .getModelManager()
                    .getStandaloneModel(ClientRegistry.ARROW_MODEL_KEYS.get(state.getValue(ElevatorBlock.FACING)));
            if (arrowModel != null) {
                arrowModel.collectParts(level, pos, state, random, parts);
            }
        }

        BlockState heldState = level.getModelData(pos).get(HELD_STATE);
        if (heldState != null) {
            BlockStateModel blockModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(heldState);

            blockModel.collectParts(level, pos, heldState, random, parts);

            return;
        }

        // Fallback / original model
        super.collectParts(level, pos, state, random, parts);
    }
}
