package xyz.vsngamer.elevator.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumMap;

public class ModItems {

    public static EnumMap<EnumDyeColor, ItemBlock> elevators = new EnumMap<EnumDyeColor, ItemBlock>(EnumDyeColor.class);

    public static void init() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            ItemBlock itemBlock = new ItemBlock(ModBlocks.elevators.get(color));
            elevators.put(color, itemBlock);
            GameRegistry.register(itemBlock, itemBlock.getBlock().getRegistryName());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        for (ItemBlock itemBlock : elevators.values())
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                    new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
    }

}
