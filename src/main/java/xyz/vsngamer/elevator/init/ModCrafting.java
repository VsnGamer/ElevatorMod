package xyz.vsngamer.elevator.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {

	public static void register() {
		for (EnumDyeColor color : EnumDyeColor.values()) {
			GameRegistry.addShapedRecipe(new ItemStack(ModItems.elevators.get(color)),
					"WWW", "WPW", "WWW",
					'W', new ItemStack(Blocks.WOOL, 1, color.getMetadata()),
					'P', Items.ENDER_PEARL
			);
		}
	}
}
