package xyz.vsngamer.elevator.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.blocks.BlockElevator;

import java.util.EnumMap;

public class ModBlocks {

    public static EnumMap<EnumDyeColor, BlockElevator> elevators = new EnumMap<EnumDyeColor, BlockElevator>(EnumDyeColor.class);

    public static void init() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator();
            block.setUnlocalizedName("elevator_" + color.getName());
            elevators.put(color, block);
            GameRegistry.register(block, new ResourceLocation(Ref.MOD_ID, "elevator_" + color.getName()));
        }
    }

}
