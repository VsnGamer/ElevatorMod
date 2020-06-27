package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.ElevatorModTab;
import xyz.vsngamer.elevatorid.init.ModConfig;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;
import xyz.vsngamer.elevatorid.util.FakeUseContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@SuppressWarnings("deprecation")
public class ElevatorBlock extends HorizontalBlock {

    public static final BooleanProperty DIRECTIONAL = BooleanProperty.create("directional");
    public static final BooleanProperty SHOW_ARROW = BooleanProperty.create("show_arrow");

    private ElevatorBlockItem item;
    private final DyeColor dyeColor;

    public ElevatorBlock(DyeColor color) {
        super(Block.Properties
                .create(Material.WOOL, color)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(0.8F)
                .variableOpacity()
                .notSolid());

        setRegistryName(ElevatorMod.ID, "elevator_" + color.getTranslationKey());
        dyeColor = color;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, DIRECTIONAL, SHOW_ARROW);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(DIRECTIONAL, false);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElevatorTileEntity();
    }

    @Override
    public boolean isReplaceable(@Nonnull BlockState state, @Nonnull BlockItemUseContext useContext) {
        return false;
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }

        ItemStack handStack = player.getHeldItem(handIn);
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile == null) {
            return ActionResultType.FAIL;
        }

        if (isValidItem(handStack)) {
            Block handBlock = Block.getBlockFromItem(handStack.getItem());
            BlockState stateToApply = handBlock.getStateForPlacement(new FakeUseContext(player, handIn, hit));

            // Try set camo
            if (stateToApply != tile.getHeldState()) {
                setCamoAndUpdate(stateToApply, tile, worldIn);
                // If we successfully set camo, don't open the menu
                return ActionResultType.SUCCESS;
            }
        }

        // Remove camo
        if (player.isCrouching() && tile.getHeldState() != null) {
            NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(pos));
            return ActionResultType.SUCCESS;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.canCreatureSpawn(state, world, pos, type, entityType);
    }

//    @Override
//    public boolean isNormalCube(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
//        return false;
//    }

    @Override
    public boolean isSideInvisible(@Nonnull BlockState state, BlockState adjacentBlockState, @Nonnull Direction side) {
        return adjacentBlockState.getBlock() instanceof BreakableBlock || super.isSideInvisible(state, adjacentBlockState, side);
    }

    @Override
    public boolean canBeConnectedTo(BlockState state, IBlockReader world, BlockPos pos, Direction facing) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null)
            return tile.getHeldState().canBeConnectedTo(world, pos, facing);
        return super.canBeConnectedTo(state, world, pos, facing);
    }

    // Collision
    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile != null && tile.getHeldState() != null)
            return tile.getHeldState().getCollisionShape(worldIn, pos, context);
        return super.getCollisionShape(state, worldIn, pos, context);
    }

    // Visual outline
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile != null && tile.getHeldState() != null)
            return tile.getHeldState().getShape(worldIn, pos, context);
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public float getSlipperiness(@Nonnull BlockState state, @Nonnull IWorldReader world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null)
            return tile.getHeldState().getSlipperiness(world, pos, entity);
        return super.getSlipperiness(state, world, pos, entity);
    }

    @Override
    public void onEntityCollision(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn) {
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile != null && tile.getHeldState() != null) {
            try {
                tile.getHeldState().getBlock().onEntityCollision(state, worldIn, pos, entityIn);
                return;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        super.onEntityCollision(state, worldIn, pos, entityIn);
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
    public BlockState updatePostPlacement(@Nonnull BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        if (!worldIn.isRemote()) {
            ElevatorTileEntity tile = getElevatorTile(worldIn, currentPos);
            if (tile != null && tile.getHeldState() != null) {
                final BlockState updatedState = tile.getHeldState().updatePostPlacement(facing, facingState, worldIn, currentPos, facingPos);
                if (updatedState != tile.getHeldState()) {
                    tile.setHeldState(updatedState);
                }
            }
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    // Redstone
    @Override
    public boolean canProvidePower(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().canConnectRedstone(world, pos, side);
        }
        return false;
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().shouldCheckWeakPower(world, pos, side);
        }
        return true;
    }

    @Override
    public int getWeakPower(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        ElevatorTileEntity tile = getElevatorTile(reader, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getWeakPower(reader, pos, direction);
        }
        return 0;
    }

    @Override
    public int getStrongPower(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        ElevatorTileEntity tile = getElevatorTile(reader, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getStrongPower(reader, pos, direction);
        }
        return 0;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getLightValue(world, pos);
        }

        return super.getLightValue(state, world, pos);
    }

    @Override
    public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    public float getAmbientOcclusionLightValue(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getAmbientOcclusionLightValue(worldIn, pos);
        }
        return super.getAmbientOcclusionLightValue(state, worldIn, pos);
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getOpacity(worldIn, pos);
        }
        return worldIn.getMaxLightLevel();
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        ElevatorTileEntity tile = getElevatorTile(world, pos);
        if (tile != null && tile.getHeldState() != null) {
            return tile.getHeldState().getSoundType(world, pos, entity);
        }
        return super.getSoundType(state, world, pos, entity);
    }

    public DyeColor getColor() {
        return dyeColor;
    }

    private boolean isValidItem(ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);

        if (stack.isEmpty()) {
            return false;
        }

        // Don't accept items
        if (!(item instanceof BlockItem)) {
            return false;
        }

        // Don't try to camouflage with itself
        if (block instanceof ElevatorBlock) {
            return false;
        }

        // Only normally rendered blocks (not chests, ...)
        if (block.getDefaultState().getRenderType() != BlockRenderType.MODEL) {
            return false;
        }

        // Only blocks with a collision box
        return block.getDefaultState().getMaterial().isSolid();
    }

    private ElevatorTileEntity getElevatorTile(IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

//        if (world instanceof ServerWorld) {
//            tile = ((ServerWorld) world).getChunkAt(pos).getTileEntity(pos);
//        } else if (world instanceof ChunkRenderCache) {
//            tile = ((ChunkRenderCache) world).getTileEntity(pos, Chunk.CreateEntityType.CHECK);
//        } else {
//        }

        // Check if it exists and is valid
        if (tile instanceof ElevatorTileEntity && tile.getType().isValidBlock(world.getBlockState(pos).getBlock())) {
            return (ElevatorTileEntity) tile;
        }

//        LogManager.getLogger(ElevatorMod.ID).debug("NULL TILE, " + world.toString());
        return null;
    }

    private void setCamoAndUpdate(BlockState newState, ElevatorTileEntity tile, World world) {
        tile.setHeldState(newState);
        world.playSound(null, tile.getPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
    }


    @Nonnull
    @Override
    public Item asItem() {
        if (item == null) {
            item = new ElevatorBlockItem();
        }
        return item;
    }

    public class ElevatorBlockItem extends BlockItem {
        ElevatorBlockItem() {
            super(ElevatorBlock.this, new Item.Properties().group(ElevatorModTab.TAB));
            ResourceLocation name = ElevatorBlock.this.getRegistryName();
            if (name != null) {
                setRegistryName(name);
            }
        }
    }
}
