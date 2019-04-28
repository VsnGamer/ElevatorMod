package xyz.vsngamer.elevator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.init.Registry;

public class ElevatorModTab extends CreativeTabs {

	public static final CreativeTabs TAB = new ElevatorModTab();

	private ElevatorModTab() {
		super("tabElevators");
		setBackgroundImageName("item_search.png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(Registry.ELEVATOR_ITEMBLOCKS.get(EnumDyeColor.WHITE));
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

}
