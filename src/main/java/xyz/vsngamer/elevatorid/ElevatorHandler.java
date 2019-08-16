package xyz.vsngamer.elevatorid;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Direction;
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
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.isAlive()) return;

        boolean sneaking = player.isSneaking();
        if (lastSneaking != sneaking) {
            lastSneaking = sneaking;
            if (sneaking)
                tryTeleport(player, Direction.DOWN);
        }

        boolean jumping = player.movementInput.jump;
        if (lastJumping != jumping) {
            lastJumping = jumping;
            if (jumping)
                tryTeleport(player, Direction.UP);
        }
    }

    private static void tryTeleport(ClientPlayerEntity player, Direction facing) {
        IBlockReader world = player.getEntityWorld();

        BlockPos fromPos = getOriginElevator(player);
        if (fromPos == null) return;

        BlockPos.MutableBlockPos toPos = new BlockPos.MutableBlockPos(fromPos);
        BlockState toState;
        while (true) {
            toPos.setY(toPos.getY() + facing.getYOffset());
            if (Math.abs(toPos.getY() - fromPos.getY()) > 256)
                break;
            toState = world.getBlockState(toPos);

            // Sends all elevators to the server (related: sameColor config)
            if (TeleportHandler.isElevator(toState) && TeleportHandler.validateTarget(world, toPos)) {
                NetworkHandler.networkHandler.sendToServer(new TeleportRequest(fromPos, toPos));
            }
        }
    }

    /**
     * Checks if a player is in (lower part of the player) or has an elevator up to 2 blocks below
     *
     * @param player the player trying to teleport
     * @return the position of the origin elevator or null if it doesn't exist or is invalid.
     */
    private static BlockPos getOriginElevator(ClientPlayerEntity player) {
        World world = player.getEntityWorld();
        BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);

        // Check the player's feet and the 2 blocks under it
        for (int i = 0; i < 3; i++) {
            if (TeleportHandler.isElevator(world.getBlockState(pos)) && TeleportHandler.validateTarget(world, pos))
                return pos;
            pos = pos.down();
        }

        // Elevator doesn't exist or it's invalid
        return null;
    }
}
