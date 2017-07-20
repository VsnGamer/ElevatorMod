package xyz.vsngamer.elevator;

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

import static xyz.vsngamer.elevator.ElevatorUtils.findDestinationElevator;
import static xyz.vsngamer.elevator.ElevatorUtils.isElevator;

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

        BlockPos toPos = findDestinationElevator(world, fromPos, fromState, facing);
        if (toPos != null)
            NetworkHandler.networkWrapper.sendToServer(new TeleportRequest(facing));
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
