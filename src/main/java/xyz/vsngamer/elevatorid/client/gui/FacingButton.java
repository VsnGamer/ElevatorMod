package xyz.vsngamer.elevatorid.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;

import java.awt.*;

class FacingButton extends Button {

    Direction direction;

    FacingButton(Point slot, String text, Direction direction, BlockPos pos) {
        super(slot.x, slot.y, 20, 20, text, but ->
                NetworkHandler.INSTANCE.sendToServer(new SetFacingPacket(direction, pos)));

        this.direction = direction;
    }

//    static ArrayList<FacingButton> getButtons(BlockPos pos) {
//        ArrayList<FacingButton> list = new ArrayList<>();
//        list.add(new FacingButton("S", Direction.SOUTH, pos));
//        list.add(new FacingButton("W", Direction.WEST, pos));
//        list.add(new FacingButton("N", Direction.NORTH, pos));
//        list.add(new FacingButton("E", Direction.EAST, pos));
//    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(minecraft, p_renderButton_1_, p_renderButton_2_);
        int j = getFGColor();
        int color;
        if (!active)
            color = 65280;
        else
            color = j | MathHelper.ceil(this.alpha * 255.0F) << 24;
        this.drawCenteredString(fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
    }
}
