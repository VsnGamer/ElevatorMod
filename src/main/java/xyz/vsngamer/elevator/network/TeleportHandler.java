package xyz.vsngamer.elevator.network;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.init.ModSounds;

public class TeleportHandler implements IMessageHandler<TeleportRequest, IMessage> {
    @Override
    public IMessage onMessage(TeleportRequest message, MessageContext ctx) {

        EntityPlayerMP player = ctx.getServerHandler().player;
        World world = player.world;
        BlockPos from = message.getFrom(), to = message.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ()) return null;

        // This ensures the player is still standing on the origin elevator
        if (player.getDistanceSqToCenter(from) > 4D) return null;

        IBlockState fromState = world.getBlockState(from);
        IBlockState toState = world.getBlockState(to);

        if (!isElevator(fromState) || !isElevator(toState)) return null;
        if (!validateTarget(world, to)) return null;

//        if (ModConfig.serverConfig.sameColor) {
//            if (fromState.getBlock() != toState.getBlock()) return null;
//        }

        player.getServerWorld().addScheduledTask(() -> {
            if (ModConfig.serverConfig.precisionTarget) {
                player.setPositionAndUpdate(to.getX() + 0.5D, to.getY() + 1D, to.getZ() + 0.5D);
            } else {
                player.setPositionAndUpdate(player.posX, to.getY() + 1D, player.posZ);
            }

            player.motionY = 0;
            world.playSound(null, to, ModSounds.teleport, SoundCategory.BLOCKS, 1F, 1F);
        });
        return null;
    }

    public static boolean validateTarget(IBlockAccess world, BlockPos target) {
        return validateTarget(world.getBlockState(target.up(1))) && validateTarget(world.getBlockState(target.up(2)));
    }

    private static boolean validateTarget(IBlockState blockState) {
        return !blockState.causesSuffocation();
    }

    public static boolean isElevator(IBlockState blockState) {
        return blockState.getBlock() instanceof BlockElevator;
    }
}
