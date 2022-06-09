package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
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
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElevatorBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        List<BakedQuad> result = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderType();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        if (layer == RenderType.cutoutMipped() && side == null) {
            if (state.getValue(ElevatorBlock.DIRECTIONAL) && state.getValue(ElevatorBlock.SHOW_ARROW)) {
                BakedModel arrowModel = dispatcher.getBlockModelShaper().getModelManager().getModel(new ResourceLocation("elevatorid", "arrow"));
                BlockModelRotation rot = BlockModelRotation.by(0, (int) state.getValue(ElevatorBlock.FACING).toYRot());
                QuadTransformer transformer = new QuadTransformer(rot.getRotation().blockCenterToCorner());

                result.addAll(transformer.processMany(arrowModel.getQuads(state, null, rand, extraData)));
            }
        }

        BlockState heldState = extraData.getData(HELD_STATE);
        if (heldState != null) {
            // Render camouflage in the correct layer
            if (ItemBlockRenderTypes.canRenderInLayer(heldState, layer)) {
                BakedModel model = dispatcher.getBlockModel(heldState);
                result.addAll(model.getQuads(heldState, side, rand, extraData));
            }
            return result;
        }

        // Fallback / original model (renders on solid)
        if (layer == RenderType.solid() || layer == null)
            result.addAll(originalModel.getQuads(state, side, rand, extraData));

        return result;
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        BlockState state = data.getData(HELD_STATE);
        if (state != null) {
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(data);
        }
        return super.getParticleIcon(data);
    }
}
