package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

class FacingControllerWrapper {

    private final HashSet<FacingButton> bakedButtons = new HashSet<>();
    private final ArrayList<Point> slots = new ArrayList<>();

    FacingControllerWrapper(int xIn, int yIn, BlockPos blockPos, Direction rotation) {
        initSlots(xIn, yIn);
        initButtons(rotation, blockPos);
    }

    private void initSlots(int xIn, int yIn) {
        slots.add(new Point(xIn + 20, yIn)); // UP
        slots.add(new Point(xIn + 40, yIn + 20)); // RIGHT
        slots.add(new Point(xIn + 20, yIn + 40)); // BOTTOM
        slots.add(new Point(xIn, yIn + 20)); //LEFT
    }

    private void initButtons(Direction rotation, BlockPos pos) {
        Collections.rotate(slots, rotation.getHorizontalIndex());
        bakedButtons.add(new FacingButton(slots.get(0), "S", Direction.SOUTH, pos));
        bakedButtons.add(new FacingButton(slots.get(1), "W", Direction.WEST, pos));
        bakedButtons.add(new FacingButton(slots.get(2), "N", Direction.NORTH, pos));
        bakedButtons.add(new FacingButton(slots.get(3), "E", Direction.EAST, pos));
    }

    HashSet<FacingButton> getButtons() {
        return bakedButtons;
    }
}

