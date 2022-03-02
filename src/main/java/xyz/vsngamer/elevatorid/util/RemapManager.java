package xyz.vsngamer.elevatorid.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
        ModList.get().getModContainerById(ElevatorMod.ID).ifPresent(e::setModContainer); // as pointed out by DaemonUmbra https://github.com/MinecraftForge/MinecraftForge/issues/6252#issuecomment-543415663
        e.getMappings(ElevatorMod.ID).forEach(blockMapping -> {
            if (blockMapping.key.getPath().contains("dir_elevator_")) {
                blockMapping.remap(
                        Registry.ELEVATOR_BLOCKS.get(DyeColor.byName(blockMapping.key.getPath().substring("dir_elevator_".length()), DyeColor.WHITE)).get()
                );
            }
        });
    }

    @SubscribeEvent
    public static void missingItems(RegistryEvent.MissingMappings<Item> e) {
        ModList.get().getModContainerById(ElevatorMod.ID).ifPresent(e::setModContainer);
        e.getMappings(ElevatorMod.ID).forEach(itemMapping -> {
            if (itemMapping.key.getPath().contains("dir_elevator_")) {
                itemMapping.remap(
                        Registry.ELEVATOR_BLOCKS.get(DyeColor.byName(itemMapping.key.getPath().substring("dir_elevator_".length()), DyeColor.WHITE)).get().asItem()
                );
            }
        });
    }
}
