package xyz.vsngamer.elevator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevator.init.ModBlocks;

public class ElevatorModTab extends CreativeTabs{

	public ElevatorModTab() {
		super("tabElevators");
	}

	@Override
	public Item getTabIconItem() {
		ItemStack iStack = new ItemStack(ModBlocks.elevator);
		return iStack.getItem();
	}

}
