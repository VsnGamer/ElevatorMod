package xyz.vsngamer.elevatorid.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel.HELD_STATE;
import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_TILE_ENTITY;

public class ElevatorTileEntity extends BlockEntity implements MenuProvider {

    private BlockState heldState;

    public ElevatorTileEntity(BlockPos pos, BlockState state) {
        super(ELEVATOR_TILE_ENTITY.get(), pos, state);
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);

        // Get blockstate from compound, always check if it's valid
        BlockState held_id = NbtUtils.readBlockState(tag.getCompound("held_id"));
        heldState = isValidState(held_id) ? held_id : null;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);

        if (heldState != null)
            tag.put("held_id", NbtUtils.writeBlockState(heldState));
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(HELD_STATE, heldState).build();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        updateClient();
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithId();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, tile -> getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.elevatorid.elevator");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inv, @Nonnull Player player) {
        return new ElevatorContainer(id, worldPosition, player);
    }

    public void setHeldState(BlockState state) {
        this.heldState = state;
        markUpdated();
    }

    private void markUpdated() {
        this.setChanged();

        if (level != null) {
            level.sendBlockUpdated(
                    getBlockPos(),
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );

            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            getBlockState().updateNeighbourShapes(level, worldPosition, 2);
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    public BlockState getHeldState() {
        return heldState;
    }

    private void updateClient() {
        if (level != null && level.isClientSide) {
            requestModelDataUpdate();
            level.sendBlockUpdated(
                    getBlockPos(),
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    public boolean setCamoAndUpdate(BlockState newState) {
        if (heldState == newState)
            return false;

        if (!isValidState(newState))
            return false;

        setHeldState(newState);
        if (getLevel() != null)
            getLevel().playSound(null, getBlockPos(), Registry.CAMOUFLAGE_SOUND.get(), SoundSource.BLOCKS, 1F, 1F);

        return true;
    }

    public static boolean isValidState(BlockState state) {
        if (state == null)
            return true;

        if (state.getBlock() == Blocks.AIR)
            return false;

        // Tile entities can cause problems
        if (state.hasBlockEntity())
            return false;

        // Don't try to camouflage with itself
        if (state.getBlock() instanceof ElevatorBlock) {
            return false;
        }

        // Only normally rendered blocks (not chests, ...)
        if (state.getRenderShape() != RenderShape.MODEL) {
            return false;
        }

        // Only blocks with a collision box
        return state.getMaterial().isSolid();
    }

//    public static BlockEntityType<ElevatorTileEntity> getType(Block... validBlocks) {
//        BlockEntityType<ElevatorTileEntity> type = BlockEntityType.Builder.of(ElevatorTileEntity::new, validBlocks).build(null);
//        type.setRegistryName("elevator_tile");
//        return type;
//    }
}
