package com.vsngarcia.neoforge;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.level.ElevatorBlockEntityBase;
import com.vsngarcia.neoforge.client.render.ElevatorBakedModel;
import com.vsngarcia.neoforge.init.Registry;
import com.vsngarcia.neoforge.tile.ElevatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;

import java.util.Optional;

public class ElevatorBlock extends ElevatorBlockBase {

    private final MapCodec<ElevatorBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(DyeColor.CODEC.fieldOf("color").forGetter(ElevatorBlockBase::getColor))
        .apply(instance, ElevatorBlock::new));

    public ElevatorBlock(DyeColor color) {
        super(color, Registry.ELEVATOR_TILE_ENTITY::get);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElevatorBlockEntity(pos, state);
    }

    @Override
    protected void openMenu(Player player, ElevatorBlockEntityBase tile, BlockPos pos) {
        player.openMenu(tile, pos);
    }

    @Override
    public boolean collisionExtendsVertically(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        Entity collidingEntity
    ) {
        return getHeldState(level, pos)
            .map(s -> s.collisionExtendsVertically(level, pos, collidingEntity))
            .orElse(super.collisionExtendsVertically(state, level, pos, collidingEntity));
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getHeldState(level, pos)
            .map(s -> s.getFriction(level, pos, entity))
            .orElse(super.getFriction(state, level, pos, entity));
    }

    @Override
    public BlockState getAppearance(
        BlockState state,
        BlockAndLightGetter level,
        BlockPos pos,
        Direction side,
        BlockState queryState,
        BlockPos queryPos
    ) {
        if (level instanceof ServerLevel) {
            return getHeldState(level, pos)
                .map(s -> s.getAppearance(level, pos, side, queryState, queryPos))
                .orElse(super.getAppearance(state, level, pos, side, queryState, queryPos));
        }

        var data = level.getModelData(pos);
        if (data == ModelData.EMPTY) {
            return super.getAppearance(state, level, pos, side, queryState, queryPos);
        }

        var heldState = data.get(ElevatorBakedModel.HELD_STATE);
        if (heldState == null) {
            return super.getAppearance(state, level, pos, side, queryState, queryPos);
        }

        return heldState.getAppearance(level, pos, side, queryState, queryPos);
    }


    @Override
    protected BlockState getAppearance(
        BlockState facingState,
        LevelReader worldIn,
        BlockPos facingPos,
        Direction opposite,
        BlockState heldState,
        BlockPos currentPos
    ) {
        return facingState.getAppearance(worldIn, facingPos, opposite, heldState, currentPos);
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(
        BlockGetter level,
        BlockPos pos,
        BlockState state,
        BlockState neighborState,
        Direction dir
    ) {
        var modelData = level.getModelData(pos);
        if (modelData == ModelData.EMPTY) {
            return super.hidesNeighborFace(level, pos, state, neighborState, dir);
        }

        var heldState = modelData.get(ElevatorBakedModel.HELD_STATE);
        if (heldState == null) {
            return super.hidesNeighborFace(level, pos, state, neighborState, dir);
        }

        var neighborModelData = level.getModelData(pos.relative(dir));
        if (neighborModelData == ModelData.EMPTY) {
            return heldState.skipRendering(neighborState, dir);
        }

        var neighborHeldState = neighborModelData.get(ElevatorBakedModel.HELD_STATE);
        return heldState.skipRendering(neighborHeldState != null ? neighborHeldState : neighborState, dir);
    }

    @Override
    public boolean hasDynamicLightEmission(BlockState state) {
        return true;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return Optional.ofNullable(level.getAuxLightManager(pos)).map(lm -> lm.getLightAt(pos)).orElse(0);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getHeldState(level, pos)
            .map(s -> s.getBlock().canConnectRedstone(s, level, pos, direction))
            .orElse(super.canConnectRedstone(state, level, pos, direction));
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return getHeldState(level, pos)
            .map(s -> s.shouldCheckWeakPower(level, pos, side))
            .orElse(super.shouldCheckWeakPower(state, level, pos, side));
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getHeldState(level, pos)
            .map(s -> s.getSoundType(level, pos, entity))
            .orElse(super.getSoundType(state, level, pos, entity));
    }
}
