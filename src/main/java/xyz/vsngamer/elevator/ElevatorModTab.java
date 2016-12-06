package xyz.vsngamer.elevator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
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
	public Item getTabIconItem() {
		return ModItems.elevators.get(EnumDyeColor.WHITE);
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}

}
