package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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

public class ElevatorBakedModel extends BakedModelWrapper<IBakedModel> {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(IBakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> result = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderLayer();
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        if (layer == RenderType.getCutoutMipped() && side == null) {
            if (state.get(ElevatorBlock.DIRECTIONAL) && state.get(ElevatorBlock.SHOW_ARROW)) {
                IBakedModel arrowModel = dispatcher.getBlockModelShapes().getModelManager().getModel(new ResourceLocation("elevatorid", "arrow"));
                ModelRotation rot = ModelRotation.getModelRotation(0, (int) state.get(ElevatorBlock.HORIZONTAL_FACING).getHorizontalAngle());
                QuadTransformer transformer = new QuadTransformer(rot.getRotation().blockCenterToCorner());

                result.addAll(transformer.processMany(arrowModel.getQuads(state, null, rand, extraData)));
            }
        }

        BlockState heldState = extraData.getData(HELD_STATE);
        if (heldState != null) {
            // Render camouflage in the correct layer
            if (RenderTypeLookup.canRenderInLayer(heldState, layer)) {
                IBakedModel model = dispatcher.getModelForState(heldState);
                result.addAll(model.getQuads(heldState, side, rand, extraData));
            }
            return result;
        }

        // Fallback / original model (renders on solid)
        if (layer == RenderType.getSolid() || layer == null)
            result.addAll(originalModel.getQuads(state, side, rand, extraData));

        return result;
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        BlockState state = data.getData(HELD_STATE);
        if (state != null) {
            return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state).getParticleTexture(data);
        }
        return super.getParticleTexture(data);
    }
}
