package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.tileentity.TileEntityElevator;

public class BlockElevatorBlue extends Block implements ITileEntityProvider{
	
	public BlockElevatorBlue() {
		super(Material.CLOTH);
		setUnlocalizedName(Ref.EBlocks.ELEVATOR_BLUE.getUnlocalizedName());
		setRegistryName(Ref.EBlocks.ELEVATOR_BLUE.getRegistryName());
		setHardness(0.8F);
		setSoundType(SoundType.CLOTH);
		setCreativeTab(ElevatorMod.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityElevator();
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		
		if(ModConfig.mobSpawn){
		return true;
		}else{
			return false;
		}
	}
}
