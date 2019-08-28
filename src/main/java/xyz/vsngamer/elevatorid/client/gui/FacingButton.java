package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

class FacingButton extends Button {
    FacingButton(int widthIn, int heightIn, String text, Direction direction) {
        super(widthIn, heightIn, 20, 20, text, but -> NetworkHandler.INSTANCE.sendToServer());
    }
}
