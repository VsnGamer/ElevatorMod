package xyz.vsngamer.elevatorid;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevatorid.init.Registry;

public class ElevatorModTab extends ItemGroup {

    ElevatorModTab() {
        super("tabElevators");
        //use Minecraft's default search bar
        setBackgroundImageName("item_search.png");
    }

//	@Override
//	@SideOnly(Side.CLIENT)
//	public ItemStack getTabIconItem() {
//		return new ItemStack(Registry.elevatorsItems.get(EnumDyeColor.WHITE));
//	}

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Registry.elevatorsItems.get(EnumDyeColor.WHITE));
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

}
