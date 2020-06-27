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
        super(slot.x, slot.y, 20, 20, ITextComponent.func_241827_a_(text), but ->
                NetworkHandler.INSTANCE.sendToServer(new SetFacingPacket(direction, pos)));

        this.direction = direction;
    }

    @Override
    public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (func_230449_g_())
            func_238468_a_(matrixStack, field_230690_l_, field_230691_m_, field_230690_l_ + 20, field_230691_m_ + 20, -2130706433,-2130706433);

        func_238471_a_(matrixStack, Minecraft.getInstance().fontRenderer,
                func_230458_i_().getString(),
                this.field_230690_l_ + this.field_230688_j_ / 2,
                this.field_230691_m_ + (this.field_230689_k_ - 8) / 2,
                field_230693_o_ ? 16777215 : 65280
        );
    }
}
