/*
package xyz.vsngamer.elevator.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.tile.TileElevator;

import javax.annotation.Nullable;

public class ElevatorAccess implements IBlockAccess {


    private final IBlockAccess access;

    public ElevatorAccess(IBlockAccess access) {
        this.access = access;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return access.getTileEntity(pos);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 0;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        IBlockState state = access.getBlockState(pos);

        if (state.getBlock() instanceof BlockElevator) {
            TileElevator tile = (TileElevator) access.getTileEntity(pos);
            if (tile != null) {
                state = tile.getCamoState();
            }
        }
        return state == null ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return false;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return access.getBiome(pos);
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return access.getStrongPower(pos, direction);
    }

    @Override
    public WorldType getWorldType() {
        return access.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return access.isSideSolid(pos, side, _default);
    }
}
*/
