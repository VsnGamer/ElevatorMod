package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import xyz.vsngamer.elevatorid.ElevatorMod;

public class ModTags {
    public static final Tag<Block> NORMAL_ELEVATORS_TAG = makeBlockWrapperTag("elevators");
    public static final Tag<Block> DIRECTIONAL_ELEVATORS_TAG = makeBlockWrapperTag("directional_elevators");
    public static final Tag<Block> ALL_ELEVATORS_TAG = makeBlockWrapperTag("all_elevators");

    public static final Tag<Item> NORMAL_ELEVATORS_ITEM_TAG = makeItemWrapperTag("elevators");
    public static final Tag<Item> DIRECTIONAL_ELEVATORS_ITEM_TAG = makeItemWrapperTag("directional_elevators");
    public static final Tag<Item> ALL_ELEVATORS_ITEM_TAG = makeItemWrapperTag("all_elevators");

    private static Tag<Block> makeBlockWrapperTag(String id) {
        return new BlockTags.Wrapper(new ResourceLocation(ElevatorMod.ID, id));
    }

    private static Tag<Item> makeItemWrapperTag(String id) {
        return new ItemTags.Wrapper(new ResourceLocation(ElevatorMod.ID, id));
    }
}
