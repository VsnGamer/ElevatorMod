package xyz.vsngamer.elevatorid.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel.HELD_STATE;
import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_TILE_ENTITY;

public class ElevatorTileEntity extends BlockEntity implements MenuProvider {

    private BlockState heldState;

    public ElevatorTileEntity(BlockPos pos, BlockState state) {
        super(ELEVATOR_TILE_ENTITY, pos, state);
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);

        // Get blockstate from compound, always check if it's valid
        if (compound.contains("held_id")) {
            BlockState held_id = NbtUtils.readBlockState(compound.getCompound("held_id"));
            heldState = ElevatorBlock.isValidState(held_id) ? held_id : null;
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);

        if (heldState != null)
            compound.put("held_id", NbtUtils.writeBlockState(heldState));

        return compound;
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(HELD_STATE, heldState).build();
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 4, getUpdateTag());
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
        updateClient();
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("screen.elevatorid.elevator");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
        return new ElevatorContainer(id, worldPosition, player);
    }

    public void setHeldState(BlockState heldState) {
        this.heldState = heldState;
        updateServer();
    }

    public BlockState getHeldState() {
        return heldState;
    }

    private void updateServer() throws IllegalStateException {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), getBlockState(), getBlockState(), 3, 0);
            level.getChunkSource().getLightEngine().checkBlock(worldPosition);
        } else {
            throw new IllegalStateException("Run this on the server");
        }
    }

    private void updateClient() {
        if (level != null && level.isClientSide) {
            ModelDataManager.requestModelDataRefresh(this);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 1);
            level.getChunkSource().getLightEngine().checkBlock(worldPosition);
        }
    }

    public static BlockEntityType<ElevatorTileEntity> getType(Block... validBlocks) {
        BlockEntityType<ElevatorTileEntity> type = BlockEntityType.Builder.of(ElevatorTileEntity::new, validBlocks).build(null);
        type.setRegistryName("elevator_tile");
        return type;
    }
}
