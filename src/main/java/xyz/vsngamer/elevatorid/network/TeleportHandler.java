package xyz.vsngamer.elevatorid.network;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;
import xyz.vsngamer.elevatorid.init.ModSounds;

import java.util.function.Supplier;

//import xyz.vsngamer.elevatorid.init.ModConfig;

public class TeleportHandler {
    static void handle(TeleportRequest message, Supplier<NetworkEvent.Context> ctx) {
        EntityPlayerMP player = ctx.get().getSender();
        if (player == null) {
            LogManager.getLogger().warn("player null");
            return;
        }
        World world = player.world;
        BlockPos from = message.getFrom(), to = message.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ()) return;
        IBlockState fromState = world.getBlockState(from);
        IBlockState toState = world.getBlockState(to);
        if (!isElevator(fromState) || !isElevator(toState)) return;
        if (player.getDistanceSqToCenter(from) > 5f) return;
        if (!validateTarget(world, to)) return;

        //TODO: Config
//        if (ModConfig.sameColor) {
//            if (fromState.getBlock() != toState.getBlock()) return;
//        }
//        if (ModConfig.precisionTarget) {
        player.setPositionAndUpdate(to.getX() + 0.5f, to.getY() + 1, to.getZ() + 0.5f);
//        } else {
//            player.setPositionAndUpdate(to.getX() - from.getX() + player.posX, to.getY() - from.getY() + player.posY, to.getZ() - from.getZ() + player.posZ);
//        }
        player.motionY = 0;
        world.playSound(null, to, ModSounds.teleport, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public static boolean validateTarget(IBlockReader world, BlockPos target) {
        return validateTarget(world.getBlockState(target.up(1))) && validateTarget(world.getBlockState(target.up(2)));
    }

    private static boolean validateTarget(IBlockState blockState) {
        return blockState.getMaterial() == Material.AIR || !blockState.isSolid();
    }

    public static boolean isElevator(IBlockState blockState) {
        return blockState.getBlock() instanceof BlockElevator;
    }
}
