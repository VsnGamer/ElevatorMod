package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import xyz.vsngamer.elevatorid.ElevatorMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ElevatorBakedModel extends BakedModelWrapper<IBakedModel> {

    private static final ModelProperty<BlockState> HELD_STATE = new ModelProperty<>();

    public ElevatorBakedModel(IBakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState heldState = extraData.getData(HELD_STATE);
        if(heldState != null){
            IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(heldState);
            // TODO
            return model.getQuads(heldState, side, rand, extraData);
        }

        // Original model
        return super.getQuads(state, side, rand, extraData);
    }

    private static IModel getArrowModel() {
        try {
            return ModelLoaderRegistry.getModel(new ResourceLocation(ElevatorMod.ID, "block/arrow"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
