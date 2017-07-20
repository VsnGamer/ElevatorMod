package xyz.vsngamer.elevator;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.init.ElevatorConfig;

import javax.annotation.Nullable;

/**
 * @author Mitchell Skaggs
 */
public final class ElevatorUtils {
    private ElevatorUtils() {
    }

    public static boolean isValidTarget(IBlockAccess world, BlockPos target) {
        return isValidTarget(world.getBlockState(target.up(1)))
                && isValidTarget(world.getBlockState(target.up(2)));
    }

    private static boolean isValidTarget(IBlockState blockState) {
        return !blockState.causesSuffocation();
    }

    @Nullable
    public static BlockPos findDestinationElevator(World world, BlockPos fromPos, IBlockState fromState, EnumFacing facing) {

        BlockPos toPos = fromPos;
        while (true) {
            toPos = toPos.up(facing.getFrontOffsetY());
            if (Math.abs(toPos.getY() - fromPos.getY()) > 256) break;
            IBlockState toState = world.getBlockState(toPos);

            if (toState.getBlock() == fromState.getBlock()
                    && (!ElevatorConfig.restrictTeleportToSameColor || fromState.getValue(BlockColored.COLOR) == toState.getValue(BlockColored.COLOR))
                    && isValidTarget(world, toPos)) {
                return toPos;
            }
        }
        return null;
    }

    public static boolean isElevator(IBlockState blockState) {
        return blockState.getBlock() instanceof BlockElevator;
    }
}
