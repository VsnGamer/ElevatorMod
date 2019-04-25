package xyz.vsngamer.elevator.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.tile.Properties;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ModelCamoElevator implements IBakedModel {

    private final IBakedModel defaultModel;

    public ModelCamoElevator(IBakedModel model) {
        defaultModel = model;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        Minecraft mc = Minecraft.getMinecraft();
        IExtendedBlockState extState = (IExtendedBlockState) state;
        IBlockState heldState = extState.getValue(Properties.HELD_STATE);

        if (heldState != null) {
            IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(heldState);
            try {
                return model.getQuads(heldState, side, rand);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Default model / fallback
        return defaultModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/wool_colored_white");
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
