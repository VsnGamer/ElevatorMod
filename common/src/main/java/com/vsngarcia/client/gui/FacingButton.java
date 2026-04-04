package com.vsngarcia.client.gui;

import com.vsngarcia.network.ClientPacketSender;
import com.vsngarcia.network.client.SetFacingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import java.awt.*;


class FacingButton extends Button {

    final Direction direction;

    FacingButton(Point slot, Direction direction, BlockPos pos, ClientPacketSender packetSender) {
        super(
                slot.x,
                slot.y,
                20,
                20,
                Component.translatable("screen.elevatorid.elevator.directional_" + direction.getName()),
                but -> {
                    packetSender.sendToServer(new SetFacingPacket(direction, pos));
                    but.setFocused(false);
                },
                DEFAULT_NARRATION
        );

        this.direction = direction;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (isHoveredOrFocused() && active) {
            graphics.fill(getX(), getY(), getX() + width, getY() + height, 0x80FFFFFF);
        }

        graphics.centeredText(
            Minecraft.getInstance().font,
            getMessage().getString(),
            getX() + this.width / 2,
            getY() + (this.height - 8) / 2,
            active ? 0xFF_E0_E0_E0 : 0xFF_00_FF_00
        );
    }
}
