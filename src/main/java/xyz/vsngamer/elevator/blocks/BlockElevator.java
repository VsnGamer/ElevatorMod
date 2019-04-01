package xyz.vsngamer.elevator.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
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
        if (!isValidItem(handStack, worldIn, pos)) {
            return false;
        }

        // #getStateForPlacement is better
        final IBlockState handState = Block.getBlockFromItem(handStack.getItem()).getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, handStack.getMetadata(), playerIn, null);

        TileElevator tile = this.getTileElevator(worldIn, pos);
        if (tile == null) {
            return false;
        }

        final SoundEvent sound = new SoundEvent(new ResourceLocation("minecraft:entity.item.pickup"));

        if (!handStack.isEmpty() && handState != tile.getCamoState()) {
            tile.setCamoState(handState);
            worldIn.notifyBlockUpdate(pos, state, state, 2);
            worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1F, 1F);
            return true;
        }

        if (handStack.isEmpty() && tile.getCamoState() != null) {
            tile.setCamoState(null);
            worldIn.notifyBlockUpdate(pos, state, state, 2);
            worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1F, 1F);
            return true;
        }
        return false;
    }

    private boolean isValidItem(ItemStack stack, IBlockAccess world, BlockPos pos) {
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

        // Only full blocks
        for (EnumFacing face : EnumFacing.values()) {
            if (block.getDefaultState().getBlockFaceShape(world, pos, face) != BlockFaceShape.SOLID) {
                return false;
            }
        }

        return true;
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
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        TileElevator tile = this.getTileElevator(world, pos);

        if (tile != null && tile.getCamoState() != null && tile.getCamoState().getBlock().getBlockLayer() != BlockRenderLayer.SOLID) {
            IBlockState adjacentBlock = world.getBlockState(pos.offset(face));
            TileElevator adjacentTile = this.getTileElevator(world, pos.offset(face));
            if (adjacentTile != null) {
                return adjacentTile.getCamoState() != tile.getCamoState();
            }
            return tile.getCamoState() != adjacentBlock;
        }
        return super.doesSideBlockRendering(state, world, pos, face);
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

    // only visual
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
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileElevator tile = this.getTileElevator(world, pos);
        if (tile != null && tile.getCamoState() != null) {
            return tile.getCamoState().getLightValue(world, pos);
        }
        return super.getLightValue(state, world, pos);
    }


    /**
     * Helper function for getting the TileElevator associated with its block
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
}
