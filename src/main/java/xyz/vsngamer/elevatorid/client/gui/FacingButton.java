package xyz.vsngamer.elevatorid.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;

import javax.annotation.Nonnull;
import java.awt.*;

class FacingButton extends Button {

    Direction direction;

    FacingButton(Point slot, String text, Direction direction, BlockPos pos) {
        super(slot.x, slot.y, 20, 20, ITextComponent.func_244388_a(text), but ->
                NetworkHandler.INSTANCE.sendToServer(new SetFacingPacket(direction, pos)));

        this.direction = direction;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (isHovered())
            fill(matrixStack, x, y, x + width, y + height, -2130706433);

        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer,
                getMessage().getString(),
                this.x + this.width / 2,
                this.y + (this.height - 8) / 2,
                active ? 16777215 : 65280
        );
    }
}
