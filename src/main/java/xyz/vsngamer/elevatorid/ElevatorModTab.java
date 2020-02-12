package xyz.vsngamer.elevatorid;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevatorid.init.Registry;

import javax.annotation.Nonnull;

public class ElevatorModTab extends ItemGroup {

    public static ItemGroup TAB = new ElevatorModTab();

    private ElevatorModTab() {
        super("elevators_tab");
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(Registry.ELEVATOR_BLOCKS.get(DyeColor.WHITE));
    }
}
