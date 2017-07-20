package xyz.vsngamer.elevator.network;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.vsngamer.elevator.init.ElevatorConfig;
import xyz.vsngamer.elevator.init.ModSounds;

import java.util.Objects;

import static xyz.vsngamer.elevator.ElevatorUtils.*;

public class TeleportHandler implements IMessageHandler<TeleportRequest, IMessage> {

    @Override
    public IMessage onMessage(TeleportRequest message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        World world = player.world;
        EnumFacing facing = message.getFacing();

        BlockPos from = new BlockPos(player.posX, player.posY, player.posZ).down();
        IBlockState fromState = world.getBlockState(from);

        BlockPos to = findDestinationElevator(world, from, fromState, facing);
        if (to == null) return null;
        IBlockState toState = world.getBlockState(to);

        if (from.getX() != to.getX()
                || from.getZ() != to.getZ()
                || (ElevatorConfig.restrictTeleportToSameColor && !Objects.equals(fromState.getValue(BlockColored.COLOR), toState.getValue(BlockColored.COLOR)))
                || !isElevator(fromState)
                || !isElevator(toState)
                || player.getDistanceSqToCenter(from) > 9f
                || !isValidTarget(world, to)) return null;

        if (ElevatorConfig.realignTeleportingPlayers) {
            player.setPositionAndUpdate(to.getX() + 0.5, to.getY() + 1, to.getZ() + 0.5);
        } else {
            Vec3i displacement = to.subtract(from);
            player.setPositionAndUpdate(displacement.getX() + player.posX, to.getY() + 1, displacement.getZ() + player.posZ);
        }

        player.motionY = 0;

        world.playSound(null, to, ModSounds.teleport, SoundCategory.BLOCKS, 1.0f, 0.75f + world.rand.nextFloat() / 2);

        return null;
    }
}
