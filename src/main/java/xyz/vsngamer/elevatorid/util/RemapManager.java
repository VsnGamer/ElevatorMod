package xyz.vsngamer.elevatorid.util;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID)
public class RemapManager {

    @SubscribeEvent
    public static void missingBlocks(RegistryEvent.MissingMappings<Block> e) {
        final String str = "dir_elevator_";
        ModList.get().getModContainerById(ElevatorMod.ID).ifPresent(e::setModContainer); // as pointed out by DaemonUmbra https://github.com/MinecraftForge/MinecraftForge/issues/6252#issuecomment-543415663
        e.getMappings().forEach(blockMapping -> {
            if (blockMapping.key.getPath().contains(str)) {
                blockMapping.remap(Registry.ELEVATOR_BLOCKS.get(DyeColor.valueOf(blockMapping.key.getPath().substring(str.length()).toUpperCase())));
            }
        });
    }

    @SubscribeEvent
    public static void missingItems(RegistryEvent.MissingMappings<Item> e) {
        final String str = "dir_elevator_";
        ModList.get().getModContainerById(ElevatorMod.ID).ifPresent(e::setModContainer);
        e.getMappings().forEach(itemMapping -> {
            if (itemMapping.key.getPath().contains(str)) {
                itemMapping.remap(Registry.ELEVATOR_BLOCKS.get(DyeColor.valueOf(itemMapping.key.getPath().substring(str.length()).toUpperCase())).asItem());
            }
        });
    }
}
