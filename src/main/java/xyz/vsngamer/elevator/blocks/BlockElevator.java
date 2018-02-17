package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.init.ModConfig;

public class BlockElevator extends Block {

    public BlockElevator() {
        super(Material.CLOTH);
        setHardness(0.8F);
        setSoundType(SoundType.CLOTH);
        setCreativeTab(ElevatorMod.CREATIVE_TAB);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return ModConfig.mobSpawn && super.canCreatureSpawn(state, world, pos, type);
    }

}

