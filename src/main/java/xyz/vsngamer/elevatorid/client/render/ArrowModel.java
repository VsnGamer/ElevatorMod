package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ArrowModel implements IModel {

    @Nullable
    @Override
    public IBakedModel bake(ModelBakery bakery, Function spriteGetter, ISprite sprite, VertexFormat format) {
        return null;
    }
}
