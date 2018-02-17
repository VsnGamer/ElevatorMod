package xyz.vsngamer.elevator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.network.NetworkHandler;
import xyz.vsngamer.elevator.network.TeleportHandler;
import xyz.vsngamer.elevator.network.TeleportRequest;

@SideOnly(Side.CLIENT)
public class ElevatorHandler {
	private static boolean lastSneaking;
	private static boolean lastJumping;

	@SubscribeEvent
	public void onInput(InputEvent inputEvent) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (!player.isSpectator()) {
			boolean sneaking = player.isSneaking();
			if (lastSneaking != sneaking) {
				lastSneaking = sneaking;
				if (sneaking)
					tryTeleport(player, EnumFacing.DOWN);
			}
			boolean jumping = player.isJumping;
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
			toPos.setY(toPos.getY() + facing.getFrontOffsetY());
			if (Math.abs(toPos.getY() - fromPos.getY()) > 256)
				break;
			toState = world.getBlockState(toPos);

			
				if (TeleportHandler.isElevator(fromState) && TeleportHandler.isElevator(toState)) {
					if (TeleportHandler.validateTarget(world, toPos)) {
						NetworkHandler.networkWrapper.sendToServer(new TeleportRequest(fromPos, toPos));
					}
					break;
				}
			
		}
	}

}
