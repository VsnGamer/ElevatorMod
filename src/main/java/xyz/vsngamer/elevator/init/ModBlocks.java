package xyz.vsngamer.elevator.init;

import java.util.EnumMap;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.blocks.BlockElevator;

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
