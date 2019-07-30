package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.ElevatorModTab;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.init.ModConfig;

public class BlockElevator extends Block {

    public BlockElevator(EnumDyeColor color) {
        super(Material.CLOTH);
        setHardness(0.8F);
        setSoundType(SoundType.CLOTH);
        setCreativeTab(ElevatorModTab.TAB);

        setRegistryName(Ref.MOD_ID, "elevator_" + color.getName());
        setUnlocalizedName("elevator_" + color.getName());
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return ModConfig.serverConfig.mobSpawn && super.canCreatureSpawn(state, world, pos, type);
    }

}

