package xyz.vsngamer.elevator.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityElevator extends TileEntity implements ITickable {

	@Override
	public void update() {
		checkPlayer();
	}

	private void checkPlayer() {
		net.minecraft.util.BlockPos pos1 = this.pos;
		net.minecraft.util.AxisAlignedBB aabb = new net.minecraft.util.AxisAlignedBB(pos1, pos1.add(1, 2, 1));
		List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		

		if (list.size() > 0) {
			EntityPlayer player1 = list.get(0);
			
			Block block = player1.worldObj.getBlockState(pos1).getBlock();

			if (player1.motionY > 0 && !(player1.isSneaking()) && !(player1.worldObj.isRemote)) {

				net.minecraft.util.BlockPos pos2 = pos1.add(0, 1, 0);

				while (pos2.getY() <= 255) {
					
					

					if (player1.worldObj.getBlockState(pos2).getBlock().equals(block)) {

						player1.setPositionAndUpdate(pos2.getX() + 0.5, pos2.getY() + 1, pos2.getZ() + 0.5);
						player1.motionY = 0;
						player1.worldObj.playSoundAtEntity(player1, "elevatorid:teleport", 1.0F, 1.0F);

						break;

					}

					pos2 = pos2.add(0, 1, 0);

				}
			} else if (player1.isSneaking() && !(player1.worldObj.isRemote)) {

				net.minecraft.util.BlockPos pos3 = pos1.add(0, -1, 0);

				while (pos3.getY() >= 0) {

					if (player1.worldObj.getBlockState(pos3).getBlock().equals(block)) {

						player1.setPositionAndUpdate(pos3.getX() + 0.5, pos3.getY() + 1, pos3.getZ() + 0.5);
						player1.setSneaking(false);
						player1.worldObj.playSoundAtEntity(player1, "elevatorid:teleport", 1.0F, 1.0F);

						break;

					}
					pos3 = pos3.add(0, -1, 0);
				}
			}
		}
	}
}
