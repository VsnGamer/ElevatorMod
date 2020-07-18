package xyz.vsngamer.elevatorid.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel.HELD_STATE;
import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_TILE_ENTITY;

public class ElevatorTileEntity extends TileEntity implements INamedContainerProvider {

    private BlockState heldState;

    public ElevatorTileEntity() {
        super(ELEVATOR_TILE_ENTITY);
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        // Get blockstate from compound
        BlockState held_id = NBTUtil.readBlockState(compound.getCompound("held_id"));
        heldState = held_id == Blocks.AIR.getDefaultState() ? null : held_id;

        super.read(state, compound);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (heldState != null)
            compound.put("held_id", NBTUtil.writeBlockState(heldState));

        return super.write(compound);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(HELD_STATE, heldState).build();
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 1, getUpdateTag());
    }


    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(),pkt.getNbtCompound());
        updateClient();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.elevatorid.elevator");
    }

    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player) {
        return new ElevatorContainer(id, pos, player);
    }

    public void setHeldState(BlockState heldState) {
        this.heldState = heldState;
        updateServer();
    }

    public BlockState getHeldState() {
        return heldState;
    }

    private void updateServer() throws IllegalStateException {
        markDirty();
        if (world != null && !world.isRemote) {
            world.markAndNotifyBlock(pos, world.getChunkAt(pos), getBlockState(), getBlockState(), 3, 0);
            world.getChunkProvider().getLightManager().checkBlock(pos);
        } else {
            throw new IllegalStateException("Run this on the server");
        }
    }

    private void updateClient() {
        if (world != null && world.isRemote) {
            ModelDataManager.requestModelDataRefresh(this);
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 1);
            world.getChunkProvider().getLightManager().checkBlock(pos);
        }
    }

    public static TileEntityType<ElevatorTileEntity> getType(Block... validBlocks) {
        TileEntityType<ElevatorTileEntity> type = TileEntityType.Builder.create(ElevatorTileEntity::new, validBlocks).build(null);
        type.setRegistryName("elevator_tile");
        return type;
    }
}
