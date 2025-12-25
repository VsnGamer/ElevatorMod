package com.vsngarcia.level;

import com.vsngarcia.ElevatorBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.CollisionContext;


public abstract class ElevatorBlockEntityBase extends BlockEntity implements MenuProvider {

    protected BlockState heldState;

    public ElevatorBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);

        heldState = valueInput.read("held_id", BlockState.CODEC).orElse(null);

        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), null, null, 0);
            level.getLightEngine().checkBlock(getBlockPos());
        }
    }

    @Override
    protected void saveAdditional(ValueOutput out) {
        super.saveAdditional(out);

        out.storeNullable("held_id", BlockState.CODEC, heldState);
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
        return !state.getCollisionShape(level, worldPosition, CollisionContext.placementContext(null)).isEmpty();
    }
}
