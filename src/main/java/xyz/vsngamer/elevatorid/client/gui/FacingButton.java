package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        this.drawCenteredString(Minecraft.getInstance().fontRenderer,
                this.getMessage(),
                this.x + this.width / 2,
                this.y + (this.height - 8) / 2,
                active ? 16777215 : 65280
        );

        if (isHovered)
            fill(x, y, x + 20, y + 20, -2130706433);
    }
}
