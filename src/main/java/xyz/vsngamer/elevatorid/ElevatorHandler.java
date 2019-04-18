package xyz.vsngamer.elevatorid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.TeleportHandler;
import xyz.vsngamer.elevatorid.network.TeleportRequest;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElevatorMod.ID)
public class ElevatorHandler {

    private static boolean lastSneaking;
    private static boolean lastJumping;

    @SubscribeEvent
    public static void onInput(InputEvent event) {
        EntityPlayerSP player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;

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

    private static void tryTeleport(EntityPlayer player, EnumFacing facing) {
        World world = player.world;

        BlockPos fromPos = getOriginElevator(world, new BlockPos(player.posX, player.posY, player.posZ));
        if (fromPos == null) return;

        IBlockState fromState = world.getBlockState(fromPos), toState;
        BlockPos.MutableBlockPos toPos = new BlockPos.MutableBlockPos(fromPos);

        while (true) {
            toPos.setY(toPos.getY() + facing.getYOffset());
            if (Math.abs(toPos.getY() - fromPos.getY()) > 256)
                break;
            toState = world.getBlockState(toPos);

            // TODO: this is not very well optimized because it sends all blocks to the server (related: sameColor config)
            if (TeleportHandler.isElevator(fromState) && TeleportHandler.isElevator(toState)) {
                if (TeleportHandler.validateTarget(world, toPos)) {
                    NetworkHandler.networkHandler.sendToServer(new TeleportRequest(fromPos, toPos));
                }
            }
        }
    }

    /**
     * Checks if a BlockPos is or has an elevator up to 2 blocks below
     *
     * @param world the player is in
     * @param pos   the position of the player when trying to teleport
     * @return the position of the origin elevator or null if it doesn't exist or is invalid.
     */
    private static BlockPos getOriginElevator(IBlockReader world, BlockPos pos) {
        IBlockState state;

        // Check 3 blocks: the passed BlockPos and the 2 blocks under it
        for (int i = 0; i < 3; i++) {
            state = world.getBlockState(pos);
            if (TeleportHandler.isElevator(state) && TeleportHandler.validateTarget(world, pos))
                return pos;
            pos = pos.down();
        }

        // Elevator doesn't exist or it's invalid
        return null;
    }
}
