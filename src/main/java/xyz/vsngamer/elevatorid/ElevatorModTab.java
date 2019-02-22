package xyz.vsngamer.elevatorid;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevatorid.init.Registry;

public class ElevatorModTab extends ItemGroup {
    ElevatorModTab() {
        super("tabElevators");
        setBackgroundImageName("item_search.png"); //use Minecraft's default search bar
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Registry.elevatorsItems.get(EnumDyeColor.WHITE));
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
