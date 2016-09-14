package xyz.vsngamer.elevator.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCrafting {

	public static void register() {
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 0), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_black), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 15), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_blue), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 11), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_brown), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 12), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_cyan), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 9), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_gray), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 7), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_green), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 13), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_light_blue), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 3), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_lime), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 5), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_magenta), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 2), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_orange), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 1), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_pink), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 6), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_purple), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 10), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_red), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 14), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_silver), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 8), 'P', Items.ENDER_PEARL);
			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elevator_yellow), "WWW", "WPW", "WWW", 'W',new ItemStack(Blocks.WOOL, 1, 4), 'P', Items.ENDER_PEARL);
	}
}
