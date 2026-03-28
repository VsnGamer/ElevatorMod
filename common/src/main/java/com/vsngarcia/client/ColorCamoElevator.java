package com.vsngarcia.client;

import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.level.ElevatorBlockEntityBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ColorCamoElevator implements BlockTintSource {

    @Override
    public int color(BlockState state) {
        return 0;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof ElevatorBlockEntityBase tile)) {
            return color(state);
        }

        var heldState = tile.getHeldState();
        if (heldState == null) {
            return color(state);
        }

        BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(heldState, 0);
        if (tintSource != null) {
            return tintSource.colorInWorld(heldState, level, pos);
        }

        return color(state);
    }
}
