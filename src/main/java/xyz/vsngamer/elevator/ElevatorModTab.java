package xyz.vsngamer.elevator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.init.ModItems;

public class ElevatorModTab extends CreativeTabs {

	public ElevatorModTab() {
		super("tabElevators");
		setBackgroundImageName("item_search.png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return ModItems.elevators.get(EnumDyeColor.WHITE).func_190903_i();
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}

}
