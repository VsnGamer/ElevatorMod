package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    private static final EnumMap<EnumDyeColor, BlockElevator> ELEVATOR_BLOCKS = new EnumMap<>(EnumDyeColor.class);
    public static final EnumMap<EnumDyeColor, ItemBlock> ELEVATOR_ITEMBLOCKS = new EnumMap<>(EnumDyeColor.class);

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator(color);
            ELEVATOR_BLOCKS.put(color, block);
            ELEVATOR_ITEMBLOCKS.put(color, block.itemBlock);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        ELEVATOR_BLOCKS.values().forEach((block) -> e.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        ELEVATOR_ITEMBLOCKS.values().forEach((item) -> e.getRegistry().register(item));
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
