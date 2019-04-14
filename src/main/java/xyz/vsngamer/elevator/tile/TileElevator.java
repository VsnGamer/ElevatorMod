package xyz.vsngamer.elevator.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TileElevator extends TileEntity {
    private IBlockState heldState;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (heldState != null) {
            ResourceLocation regName = heldState.getBlock().getRegistryName();
            if (regName != null) {
                compound.setString("held_id", regName.toString());
                compound.setInteger("held_meta", heldState.getBlock().getMetaFromState(heldState));
            }
        }

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        // Get held_id and held_meta from compound
        final String held_id = compound.getString("held_id");
        final int held_meta = compound.getInteger("held_meta");

        // Get block from held_id
        final Block block = Block.getBlockFromName(held_id);

        heldState = block != null ? block.getStateFromMeta(held_meta) : null;

        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    public IBlockState getCamoState() {
        return heldState;
    }

    public void setCamoState(IBlockState state) {
        heldState = state;
        markDirty();
    }
}