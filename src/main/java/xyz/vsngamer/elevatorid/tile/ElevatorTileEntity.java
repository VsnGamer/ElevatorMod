package xyz.vsngamer.elevatorid.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.init.ModSounds;

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
        BlockState held_id = NbtUtils.readBlockState(compound.getCompound("held_id"));
        heldState = isValidState(held_id) ? held_id : null;
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

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        updateClient();
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
//        updateClient();
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

    public void setHeldState(BlockState state) {
        this.heldState = state;
        markUpdated();
    }

    private void markUpdated() {
        this.setChanged();

        if (level != null) {
            level.sendBlockUpdated(
                    this.getBlockPos(),
                    this.getBlockState(),
                    this.getBlockState(),
                    Block.UPDATE_ALL_IMMEDIATE
            );

            getBlockState().updateNeighbourShapes(level, worldPosition, 512);
            level.updateNeighborsAt(getBlockPos(),getBlockState().getBlock());
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
                    Block.UPDATE_ALL_IMMEDIATE
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
            getLevel().playSound(null, getBlockPos(), ModSounds.CAMOUFLAGE, SoundSource.BLOCKS, 1F, 1F);

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

    public static BlockEntityType<ElevatorTileEntity> getType(Block... validBlocks) {
        BlockEntityType<ElevatorTileEntity> type = BlockEntityType.Builder.of(ElevatorTileEntity::new, validBlocks).build(null);
        type.setRegistryName("elevator_tile");
        return type;
    }
}
