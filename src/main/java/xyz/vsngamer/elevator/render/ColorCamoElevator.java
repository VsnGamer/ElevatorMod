package xyz.vsngamer.elevator.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.tile.TileElevator;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ColorCamoElevator implements IBlockColor {

    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        if (worldIn == null || pos == null) {
            return -1;
        }

        Block b = worldIn.getBlockState(pos).getBlock();
        if (b instanceof BlockElevator) {
            TileElevator tile = ((BlockElevator) b).getTileElevator(worldIn, pos);
            if (tile != null && tile.getCamoState() != null) {
                return Minecraft.getMinecraft().getBlockColors().colorMultiplier(tile.getCamoState(), worldIn, pos, tintIndex);
            }
        }
        return -1;
    }

	/*@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {

		if(worldIn != null && pos != null){
            final TileEntity tile = worldIn.getTileEntity(pos);

            if(tile instanceof TileElevator && !tile.isInvalid()){
            	final TileElevator tileElevator = (TileElevator) tile;

            	if(tileElevator.heldState != null){
            		return Minecraft.getMinecraft().getBlockColors().colorMultiplier(tileElevator.heldState, worldIn, pos, tintIndex);
            	}
            }
            return 16777215;
		}
		else{
			return 16777215;
		}
	}*/

}
