package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.tile.Properties;
import xyz.vsngamer.elevator.tile.TileElevator;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockElevator extends Block {

    public BlockElevator() {
        super(Material.CLOTH);
        setHardness(0.8F);
        setSoundType(SoundType.CLOTH);
        setCreativeTab(ElevatorMod.CREATIVE_TAB);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return ModConfig.mobSpawn && super.canCreatureSpawn(state, world, pos, type);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileElevator();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack handStack = playerIn.getHeldItemMainhand();
        if (!isValidItem(handStack)) {
            return false;
        }

        Block handBlock = Block.getBlockFromItem(handStack.getItem());

        // #getStateForPlacement is better
        IBlockState handState = handBlock.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, handStack.getMetadata(), playerIn, null);

        // Get actualstate for saving calls to #getActualState
        IBlockState stateToApply = handState.getActualState(worldIn, pos);

        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile == null) {
            return false;
        }

        if (!handStack.isEmpty() && stateToApply != tile.getCamoState()) {
            setCamoAndUpdate(stateToApply, tile, worldIn, pos);
            return true;
        }

        if (handStack.isEmpty() && tile.getCamoState() != null) {
            setCamoAndUpdate(null, tile, worldIn, pos);
            return true;
        }

        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(Properties.HELD_STATE).build();
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extState = (IExtendedBlockState) state;
        TileElevator tile = this.getTileElevator(world, pos);

        if (tile != null) {
            return extState.withProperty(Properties.HELD_STATE, tile.getCamoState());
        }

        return state;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileElevator tile = this.getTileElevator(blockAccess, pos);

        if (tile != null && tile.getCamoState() != null && tile.getCamoState().getBlock().getBlockLayer() != BlockRenderLayer.SOLID) {
            TileElevator adjacentTile = this.getTileElevator(blockAccess, pos.offset(side));
            if (adjacentTile != null) {
                return adjacentTile.getCamoState() != tile.getCamoState();
            }

            IBlockState adjacentState = blockAccess.getBlockState(pos.offset(side));
            return tile.getCamoState() != adjacentState;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        TileElevator tile = this.getTileElevator(world, pos);

        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getBlock().getSlipperiness(state, world, pos, entity);
        }

        return super.getSlipperiness(state, world, pos, entity);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        TileElevator tile = this.getTileElevator(worldIn, pos);

        if (tile != null && tile.getCamoState() != null) {
            tile.getCamoState().getBlock().onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
            return;
        }

        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getCollisionBoundingBox(worldIn, pos);
        }
        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    // Visual outline
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileElevator tile = this.getTileElevator(source, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getBoundingBox(source, pos);
        }
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile != null && tile.getCamoState() != null) {
            tile.getCamoState().addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
            return;
        }
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile != null && tile.getCamoState() != null) {
            IBlockState camoState = tile.getCamoState();
            if (camoState != camoState.getActualState(worldIn, pos)) {
                tile.setCamoState(camoState.getActualState(worldIn, pos));
            }
        }
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileElevator tile = this.getTileElevator(world, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getLightValue(world, pos);
        }
        return super.getLightValue(state, world, pos);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        TileElevator tile = getTileElevator(world, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getBlock().canConnectRedstone(tile.getCamoState(), world, pos, side);
        }
        return false;
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileElevator tile = getTileElevator(blockAccess, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getWeakPower(blockAccess, pos, side);
        }
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        TileElevator tile = getTileElevator(blockAccess, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getStrongPower(blockAccess, pos, side);
        }
        return 0;
    }

    /**
     * Gets the TileElevator associated with its block
     *
     * @param world the world or an IBlockAccess
     * @param pos   the block position in the world
     * @return the TileElevator at pos or null if it doesn't exit
     */
    @Nullable
    public TileElevator getTileElevator(IBlockAccess world, BlockPos pos) {
        // Get tile at pos
        TileEntity tile = world.getTileEntity(pos);

        // Check if it exists and is valid
        if (tile instanceof TileElevator && !tile.isInvalid()) {
            return (TileElevator) tile;
        }
        return null;
    }

    /**
     * Checks if the ItemStack has a valid ItemBlock to be used as camouflage in elevators.
     * Also accepts empty stack for removing camo.
     *
     * @param stack the ItemStack
     * @return <code>true</code> if it's a valid block / empty;
     * <code>false</code> otherwise
     */
    private boolean isValidItem(ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(stack.getItem());

        // Empty hand is valid because it's used for removing camouflage
        if (stack.isEmpty()) {
            return true;
        }

        // Don't accept items
        if (!(item instanceof ItemBlock)) {
            return false;
        }

        // Don't camo with itself
        if (block instanceof BlockElevator) {
            return false;
        }

        // Only blocks with a collision box
        return block.getDefaultState().getMaterial().blocksMovement();
    }

    /**
     * Sets the elevator's camouflage state
     * Updates the clients and lighting
     * Plays nice sound effect
     *
     * @param camoState of the block it will mimic
     * @param tile      the TileElevator associated
     * @param world     where the block is
     * @param pos       position of the block
     */
    private void setCamoAndUpdate(IBlockState camoState, TileElevator tile, World world, BlockPos pos) {
        SoundEvent sound = new SoundEvent(new ResourceLocation("minecraft:item.chorus_fruit.teleport"));
        IBlockState blockState = world.getBlockState(pos);

        tile.setCamoState(camoState);
        world.notifyBlockUpdate(pos, blockState, blockState, 2);
        world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        world.notifyNeighborsOfStateChange(pos, this, true);
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1F, 1F);
    }
}
