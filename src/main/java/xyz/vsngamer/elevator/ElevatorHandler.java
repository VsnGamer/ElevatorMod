package xyz.vsngamer.elevator;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.network.NetworkHandler;
import xyz.vsngamer.elevator.network.TeleportRequest;

import static xyz.vsngamer.elevator.network.TeleportHandler.isElevator;
import static xyz.vsngamer.elevator.network.TeleportHandler.isValidTarget;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = {Side.CLIENT}, modid = Ref.MOD_ID)
public class ElevatorHandler {
    private static boolean currentlySneaking;
    private static boolean currentlyJumping;

    private static void tryTeleport(EntityPlayer player, EnumFacing facing) {
        World world = player.world;

        BlockPos fromPos = new BlockPos(player.posX, player.posY, player.posZ).down();
        IBlockState fromState = world.getBlockState(fromPos);
        if (!isElevator(fromState))
            return;

        BlockPos toPos = fromPos;
        while (true) {
            toPos = toPos.up(facing.getFrontOffsetY());
            if (Math.abs(toPos.getY() - fromPos.getY()) > 256) break;
            IBlockState toState = world.getBlockState(toPos);

            if (toState.getBlock() == fromState.getBlock()
                    && fromState.getValue(BlockColored.COLOR) == toState.getValue(BlockColored.COLOR)
                    && isValidTarget(world, toPos)) {
                NetworkHandler.networkWrapper.sendToServer(new TeleportRequest(fromPos, toPos));
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onInput(InputEvent keyInputEvent) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        boolean sneaking = player.isSneaking();
        if (currentlySneaking != sneaking) {
            currentlySneaking = sneaking;
            if (sneaking) tryTeleport(player, EnumFacing.DOWN);
        }
        boolean jumping = player.isJumping;
        if (currentlyJumping != jumping) {
            currentlyJumping = jumping;
            if (jumping) tryTeleport(player, EnumFacing.UP);
        }
    }

}
