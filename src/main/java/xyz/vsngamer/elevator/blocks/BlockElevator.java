package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import xyz.vsngamer.elevator.ElevatorModTab;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.tile.Properties;
import xyz.vsngamer.elevator.tile.TileElevator;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockElevator extends Block {

    public BlockElevator(EnumDyeColor color) {
        super(Material.CLOTH);
        setHardness(0.8F);
        setSoundType(SoundType.CLOTH);
        setCreativeTab(ElevatorModTab.TAB);

        setRegistryName(new ResourceLocation(Ref.MOD_ID, "elevator_" + color.getName()));
        setUnlocalizedName("elevator_" + color.getName());
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
        if (worldIn.isRemote) {
            return true;
        }

        final ItemStack handStack = playerIn.getHeldItemMainhand();
        if (!isValidItem(handStack)) {
            return false;
        }

        Block handBlock = Block.getBlockFromItem(handStack.getItem());

        // #getStateForPlacement is better
        IBlockState handState = handBlock.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, handStack.getMetadata(), playerIn, null);
        // Get actual state for saving calls to #getActualState
        IBlockState stateToApply = handState.getActualState(worldIn, pos);

        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile == null || tile.isInvalid()) {
            return false;
        }

        // Set camo
        if (!handStack.isEmpty() && stateToApply != tile.getCamoState()) {
            setCamoAndUpdate(stateToApply, tile, worldIn, pos);
            return true;
        }

        // Remove camo
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

        return extState;
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
            try {
                tile.getCamoState().getBlock().onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
                return;
            } catch (IllegalArgumentException ignored) {
            }
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
            try {
                tile.getCamoState().addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
            } catch (IllegalArgumentException ignored) {
            }
            return;
        }
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile != null && tile.getCamoState() != null) {
            IBlockState camoState = tile.getCamoState();
            IBlockState actualState = camoState.getActualState(worldIn, pos);
            if (camoState != actualState) {
                tile.setCamoState(actualState);
            }
        }
        return super.getActualState(state, worldIn, pos);
    }

    // Lighting
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileElevator tile = this.getTileElevator(world, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getLightValue(world, pos);
        }
        return 0;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileElevator tile = this.getTileElevator(world, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getLightOpacity(world, pos);
        }
        return 255;
    }

    // Redstone
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
        TileElevator tile = getTileElevator(world, pos);
        if (tile != null) {
            IBlockState heldState = tile.getCamoState();
            if (heldState != null) {
                return tile.getCamoState().getBlock().shouldCheckWeakPower(heldState, world, pos, side);
            }
        }
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
     * @return true if it's a valid block or an empty hand;
     * false otherwise
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

        // Only normally rendered blocks (not chests, ...)
        if (block.getDefaultState().getRenderType() != EnumBlockRenderType.MODEL) {
            return false;
        }

        // Only blocks with a collision box
        return block.getDefaultState().getMaterial().isSolid();

    }

    /**
     * Sets the elevator's camouflage state
     * Plays nice sound effect
     *
     * @param camoState state of the block it will mimic
     * @param tile      the TileElevator associated
     * @param world     where the block exists
     * @param pos       position of the block
     */
    private void setCamoAndUpdate(IBlockState camoState, TileElevator tile, World world, BlockPos pos) {
        SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport"));
        tile.setCamoState(camoState);

        if (sound != null) {
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }


    public class ItemBlockElevator extends ItemBlock{
        public ItemBlockElevator() {
            super(BlockElevator.this);
            ResourceLocation regName = BlockElevator.this.getRegistryName();
            if (regName != null) {
                setRegistryName(regName);
            }
        }
    }
}
