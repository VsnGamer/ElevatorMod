package xyz.vsngamer.elevatorid;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevatorid.init.Registry;

public class ElevatorModTab extends ItemGroup {

    public static ItemGroup TAB = new ElevatorModTab();

    private ElevatorModTab() {
        super("tabElevators");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Registry.ELEVATOR_ITEMBLOCKS.get(EnumDyeColor.WHITE));
    }

}
