package xyz.vsngamer.elevatorid.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.MissingMappingsEvent;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID)
public class RemapManager {

    @SubscribeEvent
    public static void missingMappings(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Block> blockMapping : event.getAllMappings(ForgeRegistries.Keys.BLOCKS)) {
            if (blockMapping.getKey().getPath().contains("dir_elevator_")) {
                blockMapping.remap(
                        Registry.ELEVATOR_BLOCKS.get(DyeColor.byName(blockMapping.getKey().getPath().substring("dir_elevator_".length()), DyeColor.WHITE)).get()
                );
            }
        }

        for (MissingMappingsEvent.Mapping<Item> itemMapping : event.getAllMappings(ForgeRegistries.Keys.ITEMS)) {
            if (itemMapping.getKey().getPath().contains("dir_elevator_")) {
                itemMapping.remap(
                        Registry.ELEVATOR_BLOCKS.get(DyeColor.byName(itemMapping.getKey().getPath().substring("dir_elevator_".length()), DyeColor.WHITE)).get().asItem()
                );
            }
        }
    }
}
