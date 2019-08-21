package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.AbstractElevator;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;
import xyz.vsngamer.elevatorid.blocks.DirectionalElevatorBlock;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final EnumMap<DyeColor, AbstractElevator> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);
    private static final EnumMap<DyeColor, AbstractElevator> DIRECTIONAL_ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            ELEVATOR_BLOCKS.put(color, new BlockElevator(color));
            DIRECTIONAL_ELEVATOR_BLOCKS.put(color, new DirectionalElevatorBlock(color));
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block));
        DIRECTIONAL_ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block.asItem()));
        DIRECTIONAL_ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block.asItem()));
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
