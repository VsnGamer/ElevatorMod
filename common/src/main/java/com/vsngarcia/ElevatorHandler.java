package com.vsngarcia;

import com.vsngarcia.network.ClientPacketSender;
import com.vsngarcia.network.TeleportPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;


public class ElevatorHandler {

    private static boolean lastSneaking;
    private static boolean lastJumping;

    public static void handleInput(ClientPacketSender sender) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.isAlive() || player.input == null)
            return;

        boolean sneaking = player.input.shiftKeyDown;
        if (lastSneaking != sneaking) {
            lastSneaking = sneaking;
            if (sneaking) {
                tryTeleport(player, Direction.DOWN, sender);
            }
        }

        boolean jumping = player.input.jumping;
        if (lastJumping != jumping) {
            lastJumping = jumping;
            if (jumping) {
                tryTeleport(player, Direction.UP, sender);
            }
        }
    }

    private static void tryTeleport(LocalPlayer player, Direction facing, ClientPacketSender sender) {
        Level world = player.level();

        BlockPos fromPos = getOriginElevator(player);
        if (fromPos == null) {
            return;
        }

        BlockPos.MutableBlockPos toPos = fromPos.mutable();

        var fromElevator = TeleportPacket.getElevator(world.getBlockState(fromPos));
        if (fromElevator == null) {
            return;
        }

        while (true) {
            toPos.setY(toPos.getY() + facing.getStepY());
            if (world.isOutsideBuildHeight(toPos) || Math.abs(toPos.getY() - fromPos.getY()) > Config.GENERAL.range.get()) {
                break;
            }

            var toElevator = TeleportPacket.getElevator(world.getBlockState(toPos));
            if (toElevator != null && TeleportPacket.isValidPos(world, toPos)) {
                if (!Config.GENERAL.sameColor.get() || fromElevator.getColor() == toElevator.getColor()) {
                    sender.sendToServer(new TeleportPacket(fromPos, toPos));
                    break;
                }
            }
        }
    }

    /**
     * Checks if a player(lower part) is in or has an elevator up to 2 blocks below
     *
     * @param player the player trying to teleport
     * @return the position of the first valid elevator or null if it doesn't exist
     */
    private static BlockPos getOriginElevator(LocalPlayer player) {
        BlockPos pos = player.blockPosition();

        for (int i = 0; i < Config.GENERAL.activationRange.getAsInt(); i++) {
            if (TeleportPacket.getElevator(player.level().getBlockState(pos)) != null) {
                if (!TeleportPacket.isValidPos(player.level(), pos)) {
                    return null;
                }
                return pos;
            }
            pos = pos.below();
        }

        // Elevator doesn't exist or it's invalid
        return null;
    }
}
