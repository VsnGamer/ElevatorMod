package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.item.DyeColor;
import xyz.vsngamer.elevatorid.ElevatorMod;

public class BlockElevator extends AbstractElevator {
    public BlockElevator(DyeColor color) {
        super(color);
    }

    @Override
    void setReg(DyeColor color) {
        setRegistryName(ElevatorMod.ID, "elevator_" + color.getName());
    }
}
