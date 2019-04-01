package xyz.vsngamer.elevator.network;

import net.minecraft.block.material.Material;
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
        IBlockState fromState = world.getBlockState(from);
        IBlockState toState = world.getBlockState(to);
        //if (fromState.getBlock() != toState.getBlock() || !isElevator(fromState) || !isElevator(toState)) return null;
        if (player.getDistanceSqToCenter(from) > 4F) return null;
        if (!validateTarget(world, to)) return null;
        if (ModConfig.sameColor) {
        	if(fromState.getBlock() != toState.getBlock()) return null;
        }
        if (ModConfig.precisionTarget) {
            player.setPositionAndUpdate(to.getX() + 0.5f, to.getY() + 1, to.getZ() + 0.5f);
        } else {
            player.setPositionAndUpdate(to.getX() - from.getX() + player.posX, to.getY() - from.getY() + player.posY, to.getZ() - from.getZ() + player.posZ);
        }
        player.motionY = 0;
        world.playSound(null, to, ModSounds.teleport, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return null;
    }

    public static boolean validateTarget(IBlockAccess world, BlockPos target) {
        return validateTarget(world.getBlockState(target.up(1))) && validateTarget(world.getBlockState(target.up(2)));
    }

    private static boolean validateTarget(IBlockState blockState) {
        return blockState.getMaterial() == Material.AIR || !blockState.isOpaqueCube();
    }

    public static boolean isElevator(IBlockState blockState) {
        return blockState.getBlock() instanceof BlockElevator;
    }
}
