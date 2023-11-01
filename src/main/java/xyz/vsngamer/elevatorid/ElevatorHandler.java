package xyz.vsngamer.elevatorid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.TeleportHandler;
import xyz.vsngamer.elevatorid.network.TeleportRequest;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElevatorMod.ID)
public class ElevatorHandler {
    private static boolean lastSneaking;
    private static boolean lastJumping;

    @SubscribeEvent
    public static void onInput(InputEvent.Key event) {
        handleInput();
    }

    @SubscribeEvent
    private static void onMouseInput(InputEvent.MouseButton.Post event) {
        handleInput();
    }

    private static void handleInput() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.isAlive() || player.input == null)
            return;

        boolean sneaking = player.input.shiftKeyDown;
        if (lastSneaking != sneaking) {
            lastSneaking = sneaking;
            if (sneaking)
                tryTeleport(player, Direction.DOWN);
        }

        boolean jumping = player.input.jumping;
        if (lastJumping != jumping) {
            lastJumping = jumping;
            if (jumping)
                tryTeleport(player, Direction.UP);
        }
    }

//    @SubscribeEvent
//    public static void onLivingUpdate(TickEvent.PlayerTickEvent event) {
//        if (event.phase != TickEvent.Phase.END || !event.player.level().isClientSide)
//            return;
//
//        LocalPlayer player = (LocalPlayer) event.player;
//
//        boolean jumping = (player.getDeltaMovement().y > 0 || player.input.jumping) // for 2 block gaps where the player hits the ceiling
//                && !player.onGround();
//        if (lastJumping != jumping) {
//            lastJumping = jumping;
//            if (jumping) {
//                tryTeleport(player, Direction.UP);
//            }
//        }
//
//        boolean crouching = player.isCrouching();
//        if (lastSneaking != crouching) {
//            lastSneaking = crouching;
//            if (crouching) {
//                tryTeleport(player, Direction.DOWN);
//            }
//        }
//    }

    private static void tryTeleport(LocalPlayer player, Direction facing) {
        BlockGetter world = player.getCommandSenderWorld();

        BlockPos fromPos = getOriginElevator(player);
        if (fromPos == null) return;

        BlockPos.MutableBlockPos toPos = fromPos.mutable();

        ElevatorBlock fromElevator;
        fromElevator = (ElevatorBlock) world.getBlockState(fromPos).getBlock();

        while (true) {
            toPos.setY(toPos.getY() + facing.getStepY());
            if (world.isOutsideBuildHeight(toPos) || Math.abs(toPos.getY() - fromPos.getY()) > ModConfig.GENERAL.range.get())
                break;

            ElevatorBlock toElevator = TeleportHandler.getElevator(world.getBlockState(toPos));
            if (toElevator != null && TeleportHandler.isValidPos(world, toPos)) {
                if (!ModConfig.GENERAL.sameColor.get() || fromElevator.getColor() == toElevator.getColor()) {
                    NetworkHandler.INSTANCE.sendToServer(new TeleportRequest(fromPos, toPos));
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
        Level world = player.level();
        BlockPos pos = player.blockPosition();

        // Check the player's feet and the 2 blocks under it
        for (int i = 0; i < 3; i++) {
            if (TeleportHandler.getElevator(world.getBlockState(pos)) != null)
                return pos;
            pos = pos.below();
        }

        // Elevator doesn't exist or it's invalid
        return null;
    }
}
