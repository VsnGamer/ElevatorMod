package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

class FacingControllerWrapper {

    private final HashSet<FacingButton> buttons = new HashSet<>();

    FacingControllerWrapper(int xIn, int yIn, BlockPos pos) {
        buttons.add(new FacingButton(xIn + 20, yIn, "N", Direction.NORTH, pos));
        buttons.add(new FacingButton(xIn + 40, yIn + 20, "E", Direction.EAST, pos));
        buttons.add(new FacingButton(xIn + 20, yIn + 40, "S", Direction.SOUTH, pos));
        buttons.add(new FacingButton(xIn, yIn + 20, "W", Direction.WEST, pos));
    }

    HashSet<FacingButton> getButtons() {
        return buttons;
    }
}

