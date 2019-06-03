package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.ElevatorModTab;
import xyz.vsngamer.elevatorid.init.ModConfig;

import javax.annotation.Nullable;

public class BlockElevator extends Block {

    public final ItemBlockElevator itemBlock;

    public BlockElevator(EnumDyeColor color) {
        super(Block.Properties
                .create(Material.CLOTH)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(0.8F));

        setRegistryName(ElevatorMod.ID, "elevator_" + color.getName());
        itemBlock = new ItemBlockElevator();
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IWorldReaderBase world, BlockPos pos, EntitySpawnPlacementRegistry.SpawnPlacementType type, @Nullable EntityType<? extends EntityLiving> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.canCreatureSpawn(state, world, pos, type, entityType);
    }

    private class ItemBlockElevator extends ItemBlock {
        private ItemBlockElevator() {
            super(BlockElevator.this, new Item.Properties().group(ElevatorModTab.TAB).setNoRepair());

            ResourceLocation regName = BlockElevator.this.getRegistryName();
            if (regName != null) {
                setRegistryName(regName);
            }
        }
    }
}
