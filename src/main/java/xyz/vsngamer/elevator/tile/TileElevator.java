package xyz.vsngamer.elevator.tile;

import net.darkhax.bookshelf.block.tileentity.TileEntityBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileElevator extends TileEntityBasic {

	public IBlockState heldState;

	@Override
	public void readNBT(NBTTagCompound compound) {
		final Block heldBlock = Block.getBlockFromName(compound.getString("HeldBlockId"));

		if (heldBlock != null) {
			this.heldState = heldBlock.getStateFromMeta(compound.getInteger("HeldBlockMeta"));
		}
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		if (this.heldState != null && this.heldState.getBlock() != null && this.heldState.getBlock().getRegistryName() != null) {
			compound.setString("HeldBlockId", this.heldState.getBlock().getRegistryName().toString());
			compound.setInteger("HeldBlockMeta", this.heldState.getBlock().getMetaFromState(this.heldState));
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
	}

}