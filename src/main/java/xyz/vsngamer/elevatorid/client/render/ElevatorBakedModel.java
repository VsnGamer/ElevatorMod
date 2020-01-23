package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class ElevatorBakedModel extends BakedModelWrapper<IBakedModel> {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public static final EnumMap<Direction, IBakedModel> ARROW_VARIANTS = new EnumMap<>(Direction.class);

    public ElevatorBakedModel(IBakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> list = new ArrayList<>();

        RenderType layer = MinecraftForgeClient.getRenderLayer();
        if (layer == RenderType.cutoutMipped()) {
            if (state.get(ElevatorBlock.DIRECTIONAL) && state.get(ElevatorBlock.SHOW_ARROW)) {
                list.addAll(ARROW_VARIANTS.get(state.get(ElevatorBlock.HORIZONTAL_FACING)).getQuads(state, side, rand, extraData));
            }
            return list;
        }

        BlockState heldState = extraData.getData(HELD_STATE);
        if (heldState != null) {
            IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(heldState);
            list.addAll(model.getQuads(heldState, side, rand, extraData));
            return list;
        }

        // Fallback / original model
        list.addAll(originalModel.getQuads(state, side, rand, extraData));
        return list;
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        BlockState state = data.getData(HELD_STATE);
        IBakedModel model;
        if (state != null) {
            model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
            return model.getParticleTexture(data);
        }
        return super.getParticleTexture(data);
    }
}
