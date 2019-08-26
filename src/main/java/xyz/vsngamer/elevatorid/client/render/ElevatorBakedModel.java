package xyz.vsngamer.elevatorid.client.render;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.MultipartBakedModel;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ElevatorBakedModel extends BakedModelWrapper<IBakedModel> {

    public static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    private IBakedModel arrowBakedModel;

    public ElevatorBakedModel(IBakedModel originalModel, IBakedModel arrow) {
        super(originalModel);
        arrowBakedModel = arrow;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> list = Lists.newArrayList();
        list.addAll(arrowBakedModel.getQuads(state, side, rand, extraData));

        BlockState heldState = extraData.getData(HELD_STATE);
        if (heldState != null) {
            IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(heldState);
            list.addAll(model.getQuads(state, side, rand, extraData));
            return list;
        }

        // Original model
        list.addAll(originalModel.getQuads(state, side, rand, extraData));
        return list;
    }


}
