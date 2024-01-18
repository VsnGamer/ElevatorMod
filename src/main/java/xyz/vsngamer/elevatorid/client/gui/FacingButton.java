package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;

import javax.annotation.Nonnull;
import java.awt.*;

class FacingButton extends Button {

    final Direction direction;

    FacingButton(Point slot, Direction direction, BlockPos pos) {
        super(
                slot.x,
                slot.y,
                20,
                20,
                Component.translatable("screen.elevatorid.elevator.directional_" + direction.getName()),
                but -> NetworkHandler.INSTANCE.send(new SetFacingPacket(direction, pos), PacketDistributor.SERVER.noArg()),
                DEFAULT_NARRATION
        );

        this.direction = direction;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (isHoveredOrFocused())
            guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, -2130706433);

        guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                getMessage().getString(),
                getX() + this.width / 2,
                getY() + (this.height - 8) / 2,
                active ? 16777215 : 65280
        );
    }
}
