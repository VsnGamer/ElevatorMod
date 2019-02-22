package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nullable;

public class BlockElevator extends Block {

    public BlockElevator() {
        super(Block.Properties.create(Material.CLOTH)
                .hardnessAndResistance(0.8F)
                .sound(SoundType.CLOTH));
    }

    @Override //TODO: Fix Config
    public boolean canCreatureSpawn(IBlockState state, IWorldReaderBase world, BlockPos pos, EntitySpawnPlacementRegistry.SpawnPlacementType type, @Nullable EntityType<? extends EntityLiving> entityType) {
        //        return ModConfig.mobSpawn && super.canCreatureSpawn(state, world, pos, type);
        return false;
    }
}
