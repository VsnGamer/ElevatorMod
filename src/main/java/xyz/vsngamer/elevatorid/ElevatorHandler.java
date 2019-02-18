package xyz.vsngamer.elevatorid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.TeleportHandler;
import xyz.vsngamer.elevatorid.network.TeleportRequest;

//TODO FIX REGISTRY OF THIS
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElevatorHandler {
    private static boolean lastSneaking;
    private static boolean lastJumping;

    @SubscribeEvent
    public static void onInput(InputEvent event) {
        EntityPlayerSP player = Minecraft.getInstance().player;
        if (!player.isSpectator()) {
            boolean sneaking = player.isSneaking();
            if (lastSneaking != sneaking) {
                lastSneaking = sneaking;
                if (sneaking)
                    tryTeleport(player, EnumFacing.DOWN);
            }
            boolean jumping = player.movementInput.jump;
            if (lastJumping != jumping) {
                lastJumping = jumping;
                if (jumping)
                    tryTeleport(player, EnumFacing.UP);
            }
        }
    }

    private static void tryTeleport(EntityPlayer player, EnumFacing facing) {
        World world = player.world;
        IBlockState fromState = null, toState;
        BlockPos fromPos = new BlockPos(player.posX, player.posY + 0.5f, player.posZ);
        boolean elevator = false;
        for (int i = 0; i <= 2; i++) {
            fromState = world.getBlockState(fromPos);
            if (elevator = TeleportHandler.isElevator(fromState))
                break;
            fromPos = fromPos.down();
        }
        if (!elevator)
            return;
        BlockPos.MutableBlockPos toPos = new BlockPos.MutableBlockPos(fromPos);
        while (true) {
            toPos.setY(toPos.getY() + facing.getYOffset());
            if (Math.abs(toPos.getY() - fromPos.getY()) > 256)
                break;
            toState = world.getBlockState(toPos);

            if (TeleportHandler.isElevator(fromState) && TeleportHandler.isElevator(toState)) {
                if (TeleportHandler.validateTarget(world, toPos)) {
                    NetworkHandler.networkHandler.sendToServer(new TeleportRequest(fromPos, toPos));
                }
            }
        }
    }
}