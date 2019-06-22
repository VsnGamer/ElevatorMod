package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final EnumMap<DyeColor, BlockElevator> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);
    //public static final EnumMap<DyeColor, IItemProvider> ELEVATOR_ITEMBLOCKS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            BlockElevator block = new BlockElevator(color);
            ELEVATOR_BLOCKS.put(color, block);
            //ELEVATOR_ITEMBLOCKS.put(color, block.asItem());
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        ELEVATOR_BLOCKS.values().forEach((block) -> e.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        ELEVATOR_BLOCKS.values().forEach((block) -> e.getRegistry().register(block.blockItem));
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
