package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.SetFacingPacket;

class FacingButton extends Button {

    Direction direction;

    FacingButton(int widthIn, int heightIn, String text, Direction direction, BlockPos pos) {
        super(widthIn, heightIn, 20, 20, text, but ->
                NetworkHandler.INSTANCE.sendToServer(new SetFacingPacket(direction, pos)));

        this.direction = direction;
    }
}
