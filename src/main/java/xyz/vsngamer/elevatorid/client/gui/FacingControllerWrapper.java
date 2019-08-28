package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.util.Direction;

import java.util.HashSet;

class FacingControllerWrapper {

    private final HashSet<FacingButton> buttons = new HashSet<>();

    FacingControllerWrapper(int xIn, int yIn) {
        buttons.add(new FacingButton(xIn + 20, yIn, "N", Direction.NORTH));
        buttons.add(new FacingButton(xIn + 40, yIn + 20, "E"));
        buttons.add(new FacingButton(xIn + 20, yIn + 40, "S"));
        buttons.add(new FacingButton(xIn, yIn + 20, "W"));
    }

    HashSet<FacingButton> getButtons() {
        return buttons;
    }
}

