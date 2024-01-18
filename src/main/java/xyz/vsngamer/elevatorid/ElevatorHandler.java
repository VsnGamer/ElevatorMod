package xyz.vsngamer.elevatorid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
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
    public static void onInput(InputEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.isAlive())
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
                    NetworkHandler.INSTANCE.send(new TeleportRequest(fromPos, toPos), PacketDistributor.SERVER.noArg());
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
