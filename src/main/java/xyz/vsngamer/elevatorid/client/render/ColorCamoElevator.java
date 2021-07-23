package xyz.vsngamer.elevatorid.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ColorCamoElevator implements BlockColor {

    @Override
    public int getColor(@Nonnull BlockState state, @Nullable BlockAndTintGetter lightReader, @Nullable BlockPos pos, int tintIndex) {
        if (lightReader == null || pos == null) {
            return -1;
        }

        if (state.getBlock() instanceof ElevatorBlock && lightReader.getBlockEntity(pos) instanceof ElevatorTileEntity tile) {
            if (tile.getHeldState() != null) {
                return Minecraft.getInstance().getBlockColors().getColor(tile.getHeldState(), lightReader, pos, tintIndex);
            }
        }
        return -1;
    }
}
