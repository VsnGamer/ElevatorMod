package xyz.vsngamer.elevatorid.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;
import xyz.vsngamer.elevatorid.util.FakeUseContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_TILE_ENTITY;

@SuppressWarnings("deprecation")
public class ElevatorBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final BooleanProperty DIRECTIONAL = BooleanProperty.create("directional");
    public static final BooleanProperty SHOW_ARROW = BooleanProperty.create("show_arrow");

    private static final MapCodec<ElevatorBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(ElevatorBlock::getColor))
                    .apply(instance, ElevatorBlock::new)
    );

    private final DyeColor dyeColor;

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public ElevatorBlock(DyeColor color) {
        super(Block.Properties
                .of()
                .mapColor(color)
                .sound(SoundType.WOOL)
                .strength(0.8F)
                .dynamicShape()
                .noOcclusion()
                .forceSolidOn()
        );

        dyeColor = color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRECTIONAL, SHOW_ARROW);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(DIRECTIONAL, false)
                .setValue(SHOW_ARROW, true);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ElevatorTileEntity(pos, state);
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext useContext) {
        return false;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        return getElevatorTile(worldIn, pos)
                .map(tile -> {
                    Block handBlock = Block.byItem(player.getItemInHand(handIn).getItem());
                    BlockState stateToApply = handBlock.getStateForPlacement(new FakeUseContext(player, handIn, hit));
                    if (stateToApply != null && tile.setCamoAndUpdate(stateToApply))
                        return InteractionResult.SUCCESS; // If we successfully set camo, don't open the menu

                    // Remove camo
                    if (player.isCrouching() && tile.getHeldState() != null) {
                        tile.setCamoAndUpdate(null);
                        return InteractionResult.SUCCESS;
                    }

                    NetworkHooks.openScreen((ServerPlayer) player, tile, pos);
                    return InteractionResult.SUCCESS;
                })
                .orElse(InteractionResult.FAIL);
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.isValidSpawn(state, world, pos, type, entityType);
    }

    // Collision
    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return getHeldState(worldIn, pos)
                .map(s -> s.getCollisionShape(worldIn, pos, context))
                .orElse(super.getCollisionShape(state, worldIn, pos, context));
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter world, BlockPos pos, Entity collidingEntity) {
        return getHeldState(world, pos)
                .map(s -> s.collisionExtendsVertically(world, pos, collidingEntity))
                .orElse(super.collisionExtendsVertically(state, world, pos, collidingEntity));
    }

    @Override
    public boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return getHeldState(world, pos)
                .map(s -> s.isCollisionShapeFullBlock(world, pos))
                .orElse(super.isCollisionShapeFullBlock(state, world, pos));
    }

    // Visual outline
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return getHeldState(worldIn, pos)
                .map(s -> s.getShape(worldIn, pos, context))
                .orElse(super.getShape(state, worldIn, pos, context));
    }

    @Override
    public float getFriction(@Nonnull BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        return getHeldState(world, pos)
                .map(s -> s.getFriction(world, pos, entity))
                .orElse(super.getFriction(state, world, pos, entity));
    }

    // TODO soulsand and honey use this
    @Override
    public float getSpeedFactor() {
        return super.getSpeedFactor();
    }

    // TODO honey uses this
    @Override
    public float getJumpFactor() {
        return super.getJumpFactor();
    }

    @Nonnull
    @Override
    public BlockState updateShape(@Nonnull BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, LevelAccessor worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (!worldIn.isClientSide()) {
            getElevatorTile(worldIn, currentPos)
                    .ifPresent(t -> {
                        if (t.getHeldState() == null) {
                            return;
                        }

                        BlockState appearance = facingState.getAppearance(worldIn, facingPos, facing.getOpposite(), t.getHeldState(), currentPos);
                        BlockState updatedState = t.getHeldState().updateShape(facing, appearance, worldIn, currentPos, facingPos);
                        if (updatedState != t.getHeldState()) {
                            t.setHeldState(updatedState);
                            t.setChanged();
                        }
                    });
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // Redstone
    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return getHeldState(world, pos)
                .map(s -> s.getBlock().canConnectRedstone(s, world, pos, side))
                .orElse(super.canConnectRedstone(state, world, pos, side));
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return getHeldState(level, pos)
                .map(s -> s.shouldCheckWeakPower(level, pos, side))
                .orElse(super.shouldCheckWeakPower(state, level, pos, side));
    }

    @Override
    public int getSignal(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return getHeldState(reader, pos)
                .map(s -> s.getSignal(reader, pos, direction))
                .orElse(super.getSignal(state, reader, pos, direction));
    }

    @Override
    public int getDirectSignal(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return getHeldState(reader, pos)
                .map(s -> s.getDirectSignal(reader, pos, direction))
                .orElse(super.getDirectSignal(state, reader, pos, direction));
    }

    // Light
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return getHeldState(world, pos)
                .map(s -> s.getLightEmission(world, pos))
                .orElse(super.getLightEmission(state, world, pos));
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return getHeldState(reader, pos)
                .map(s -> s.propagatesSkylightDown(reader, pos))
                .orElse(false);
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return getHeldState(worldIn, pos)
                .map(s -> s.getLightBlock(worldIn, pos))
                .orElse(worldIn.getMaxLightLevel());
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return getHeldState(world, pos)
                .map(s -> s.getOcclusionShape(world, pos))
                .orElse(super.getOcclusionShape(state, world, pos));
    }


    @Override
    public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState otherState, @NotNull Direction side) {
        return super.skipRendering(state, otherState, side);
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        // more hacks...
        Optional<BlockState> heldState = getHeldState(level, pos);
        Optional<BlockState> otherHeldState = getHeldState(level, pos.relative(dir));
        if (heldState.isPresent() && otherHeldState.isPresent()) {
            return heldState.get().skipRendering(otherHeldState.get(), dir);
        }

        return heldState
                .map(s -> s.skipRendering(neighborState, dir))
                .orElse(super.hidesNeighborFace(level, pos, state, neighborState, dir));
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        return getHeldState(level, pos).orElse(super.getAppearance(state, level, pos, side, queryState, queryPos));
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
        return getHeldState(world, pos)
                .map(s -> s.getSoundType(world, pos, entity))
                .orElse(super.getSoundType(state, world, pos, entity));
    }

    public DyeColor getColor() {
        return dyeColor;
    }

    private Optional<ElevatorTileEntity> getElevatorTile(BlockGetter world, BlockPos pos) {
        if (world == null || pos == null) {
            return Optional.empty();
        }

        return world.getBlockEntity(pos, ELEVATOR_TILE_ENTITY.get());
    }

    private Optional<BlockState> getHeldState(BlockGetter world, BlockPos pos) {
        return getElevatorTile(world, pos).map(ElevatorTileEntity::getHeldState);
    }
}
