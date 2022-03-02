package xyz.vsngamer.elevatorid;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import xyz.vsngamer.elevatorid.init.Registry;

import javax.annotation.Nonnull;

public class ElevatorModTab extends CreativeModeTab {

    public static CreativeModeTab TAB = new ElevatorModTab();

    private ElevatorModTab() {
        super("elevators_tab");
    }

    @Override
    @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(Registry.ELEVATOR_BLOCKS.get(DyeColor.WHITE).get());
    }
}
