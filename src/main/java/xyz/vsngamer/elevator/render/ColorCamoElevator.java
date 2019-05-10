package xyz.vsngamer.elevator.render;

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

        if (state.getBlock() instanceof BlockElevator) {
            TileElevator tile = ((BlockElevator) state.getBlock()).getTileElevator(worldIn, pos);
            if (tile != null && tile.getCamoState() != null) {
                return Minecraft.getMinecraft().getBlockColors().colorMultiplier(tile.getCamoState(), worldIn, pos, tintIndex);
            }
        }
        return -1;
    }
}
