package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ColorCamoElevator implements IBlockColor {

    @Override
    public int getColor(@Nonnull BlockState state, @Nullable ILightReader lightReader, @Nullable BlockPos pos, int tintIndex) {
        if (lightReader == null || pos == null) {
            return -1;
        }

        if (state.getBlock() instanceof ElevatorBlock && lightReader.getTileEntity(pos) instanceof ElevatorTileEntity) {
            ElevatorTileEntity tile = (ElevatorTileEntity) lightReader.getTileEntity(pos);
            if (tile != null && tile.getHeldState() != null) {
                return Minecraft.getInstance().getBlockColors().getColor(tile.getHeldState(), lightReader, pos, tintIndex);
            }
        }
        return -1;
    }
}
