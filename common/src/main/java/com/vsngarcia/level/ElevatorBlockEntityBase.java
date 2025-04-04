package com.vsngarcia.level;

import com.vsngarcia.ElevatorBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public abstract class ElevatorBlockEntityBase extends BlockEntity implements MenuProvider {

    protected BlockState heldState;

    public ElevatorBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider holder) {
        super.loadAdditional(tag, holder);
        if (tag.contains("held_id")) {
            // Get blockstate from compound, always check if it's valid
            BlockState state = NbtUtils.readBlockState(
                    this.level != null ? this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK,
                    tag.getCompoundOrEmpty("held_id")
            );
            heldState = isValidState(state) ? state : null;
        } else {
            heldState = null;
        }

        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), null, null, 0);
            level.getLightEngine().checkBlock(getBlockPos());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider holder) {
        super.saveAdditional(tag, holder);

        if (heldState != null) {
            tag.put("held_id", NbtUtils.writeBlockState(heldState));
        } else {
            tag.putBoolean("held_id", false);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider holder) {
        return saveCustomOnly(holder);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.elevatorid.elevator");
    }


    public void setHeldState(BlockState state) {
        if (!isValidState(state)) {
            return;
        }

        this.heldState = state;
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            level.getLightEngine().checkBlock(getBlockPos());
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());

            // Hack to update our own shape. For example, connect to other elevator camouflaged as a fence
            // Vanilla blocks' getStateForPlacement should be patched to use getAppearance, making this unnecessary
            // Also doesn't fix vanilla blocks not connecting to us, their updateShape should also be patched
            getBlockState().updateNeighbourShapes(level, getBlockPos(), 0);
            if (heldState != null) {
                for (Direction direction : Direction.values()) {
                    getBlockState().updateShape(
                            level,
                            level,
                            getBlockPos(),
                            direction,
                            getBlockPos().relative(direction),
                            level.getBlockState(getBlockPos().relative(direction)),
                            level.random
                    );
                }
            }
        }
    }

    public BlockState getHeldState() {
        return heldState;
    }

    public boolean setCamoAndUpdate(BlockState newState) {
        if (heldState == newState) return false;

        if (!isValidState(newState)) return false;

        setHeldState(newState);
        if (getLevel() != null) {
            getLevel().playSound(null, getBlockPos(), camouflageSound(), SoundSource.BLOCKS, 1F, 1F);
        }

        return true;
    }

    protected abstract SoundEvent camouflageSound();

    public boolean isValidState(BlockState state) {
        if (state == null) return true;

        if (state.isAir()) return false;

        // Tile entities can cause problems
//        if (state.hasBlockEntity()) return false;

        // Don't try to camouflage with itself
        if (state.getBlock() instanceof ElevatorBlockBase) {
            return false;
        }

        // Only normally rendered blocks (not chests, ...)
        if (state.getRenderShape() != RenderShape.MODEL) {
            return false;
        }

        // Only blocks with a collision box
        return !state.getCollisionShape(level, worldPosition).isEmpty();
    }
}
