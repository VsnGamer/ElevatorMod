package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ElevatorBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull ModelData data) {
        BlockState state = data.get(HELD_STATE);
        if (state != null) {
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(data);
        }

        return super.getParticleIcon(data);
    }

    @NotNull
    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        BlockState heldState = data.get(HELD_STATE);
        ChunkRenderTypeSet types;
        if (heldState != null)
            types = Minecraft.getInstance().getBlockRenderer().getBlockModel(heldState).getRenderTypes(heldState, rand, data);
        else
            types = super.getRenderTypes(state, rand, data);

        return ChunkRenderTypeSet.union(types, ChunkRenderTypeSet.of(RenderType.cutoutMipped()));
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        List<BakedQuad> result = new ArrayList<>();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        // Directional arrow
        if (renderType == RenderType.cutoutMipped() && side == null) {
            if (state.getValue(ElevatorBlock.DIRECTIONAL) && state.getValue(ElevatorBlock.SHOW_ARROW)) {
                BakedModel arrowModel = dispatcher.getBlockModelShaper().getModelManager().getModel(new ResourceLocation("elevatorid", "arrow"));
                BlockModelRotation rot = BlockModelRotation.by(0, (int) state.getValue(ElevatorBlock.FACING).toYRot());
                IQuadTransformer transformer = QuadTransformers.applying(rot.getRotation().blockCenterToCorner());


                result.addAll(transformer.process(arrowModel.getQuads(state, null, rand, extraData, renderType)));
            }
        }

        BlockState heldState = extraData.get(HELD_STATE);
        if (heldState != null) {
            var types = Minecraft.getInstance()
                    .getBlockRenderer()
                    .getBlockModel(heldState)
                    .getRenderTypes(heldState, rand, ModelData.EMPTY);

            if (renderType == null || types.contains(renderType)) {
                BakedModel model = dispatcher.getBlockModel(heldState);
                result.addAll(model.getQuads(heldState, side, rand, extraData, renderType));
            }

            return result;
        }

        // Fallback / original model
        result.addAll(originalModel.getQuads(state, side, rand, extraData, renderType));
        return result;
    }
}
