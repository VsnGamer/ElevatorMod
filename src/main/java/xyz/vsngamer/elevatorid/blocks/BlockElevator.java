package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.ElevatorModTab;
import xyz.vsngamer.elevatorid.init.ModConfig;

import javax.annotation.Nullable;
import java.util.List;

public class BlockElevator extends Block {

    public final BlockItemElevator blockItem;

    public BlockElevator(DyeColor color) {
        super(Block.Properties
                .create(Material.WOOL)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(0.8F));

        setRegistryName(ElevatorMod.ID, "elevator_" + color.getName());

        blockItem = new BlockItemElevator();
        //ItemTags.getCollection().getOrCreate(new ResourceLocation(ElevatorMod.ID,"elevators")).contains(blockItem);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.canCreatureSpawn(state, world, pos, type, entityType);
    }

    private class BlockItemElevator extends BlockItem {
        private BlockItemElevator() {
            super(BlockElevator.this,new Item.Properties().group(ElevatorModTab.TAB));
            this.setRegistryName(BlockElevator.this.getRegistryName());
        }
    }
}
