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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.ElevatorModTab;
import xyz.vsngamer.elevatorid.init.ModConfig;

import javax.annotation.Nullable;

public class BlockElevator extends Block {

    public BlockElevator(EnumDyeColor color) {
        super(Block.Properties.create(Material.CLOTH)
                .hardnessAndResistance(0.8F)
                .sound(SoundType.CLOTH));

        setRegistryName(ElevatorMod.ID, "elevator_" + color.getName());
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IWorldReaderBase world, BlockPos pos, EntitySpawnPlacementRegistry.SpawnPlacementType type, @Nullable EntityType<? extends EntityLiving> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.canCreatureSpawn(state, world, pos, type, entityType);
    }


    public class ItemBlockElevator extends ItemBlock {

        public ItemBlockElevator() {
            super(BlockElevator.this, new Item.Properties().group(ElevatorModTab.TAB).setNoRepair());

            if (BlockElevator.this.getRegistryName() == null) return; // This should never happen... I hope
            setRegistryName(BlockElevator.this.getRegistryName());
        }
    }
}
