package xyz.vsngamer.elevator.blocks;

import net.darkhax.bookshelf.data.Blockstates;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.tile.TileElevator;

public class BlockElevator extends BlockContainer implements ITileEntityProvider {

	public BlockElevator() {
		super(Material.CLOTH);
		setHardness(0.8F);
		setSoundType(SoundType.CLOTH);
		setCreativeTab(ElevatorMod.CREATIVE_TAB);
		setDefaultState(((IExtendedBlockState) blockState.getBaseState()).withProperty(Blockstates.HELD_STATE, null)
				.withProperty(Blockstates.BLOCK_ACCESS, null).withProperty(Blockstates.BLOCKPOS, null));
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos,
			EntityLiving.SpawnPlacementType type) {
		return ModConfig.mobSpawn && super.canCreatureSpawn(state, world, pos, type);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileElevator();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		final ItemStack stack = playerIn.getHeldItemMainhand();

		if (tile instanceof TileElevator && !tile.isInvalid()) {
			final TileElevator tileElevator = (TileElevator) tile;
			// final Block block = Block.getBlockFromItem(stack.getItem());
			final IBlockState handState = Block.getBlockFromItem(stack.getItem())
					.getStateFromMeta(stack.getItemDamage());
			if (handState != null && isValidBlock(handState) && handState != tileElevator.heldState) {
				tileElevator.heldState = handState;
				worldIn.notifyBlockUpdate(pos, state, state, 8);
				worldIn.playSound(playerIn, pos.getX(), pos.getY(), pos.getZ(), new SoundEvent(new ResourceLocation("minecraft:entity.item.pickup")), null, 1F, 1F);

				return true;

			} else if (stack.isEmpty() && tileElevator.heldState != null) {
				tileElevator.heldState = null;
				worldIn.notifyBlockUpdate(pos, state, state, 8);
				worldIn.playSound(playerIn, pos.getX(), pos.getY(), pos.getZ(), new SoundEvent(new ResourceLocation("minecraft:entity.item.pickup")), null, 1F, 1F);

			}
		}
		return false;
	}

	private static boolean isValidBlock(IBlockState state) {

		final Block block = state.getBlock();
		return (block.isOpaqueCube(state) || block.getRenderType(state) == EnumBlockRenderType.MODEL)
				&& !block.hasTileEntity(state) && (block.getMaterial(state) != Material.AIR) && (block.isBlockNormalCube(state) || block.getMaterial(state) == Material.GLASS);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {

		return EnumBlockRenderType.MODEL;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {},
				new IUnlistedProperty[] { Blockstates.HELD_STATE, Blockstates.BLOCK_ACCESS, Blockstates.BLOCKPOS });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {

		state = ((IExtendedBlockState) state).withProperty(Blockstates.BLOCK_ACCESS, world)
				.withProperty(Blockstates.BLOCKPOS, pos);

		if (world.getTileEntity(pos) instanceof TileElevator) {

			final TileElevator tile = (TileElevator) world.getTileEntity(pos);

			return ((IExtendedBlockState) state).withProperty(Blockstates.HELD_STATE, tile.heldState);
		} else {
			return state;
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {

		return 0;

	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();

	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		final TileEntity tile = blockAccess.getTileEntity(pos);

        if (tile instanceof TileElevator && !tile.isInvalid()) {

            final TileElevator elevatorTile = (TileElevator) tile;

            if (elevatorTile.heldState != null) {

                if (elevatorTile.heldState.getBlock() == Blocks.GLASS || elevatorTile.heldState.getBlock() == Blocks.GLASS_PANE) {

                    final IBlockState connected = blockAccess.getBlockState(pos.offset(side));

                    if (connected == elevatorTile.heldState) {
                        return false;
                    }
                    else if (connected.getBlock() instanceof BlockElevator) {
                        return ((TileElevator) blockAccess.getTileEntity(pos.offset(side))).heldState != elevatorTile.heldState;
                    }
                }
                
                try {

                    return elevatorTile.heldState.shouldSideBeRendered(blockAccess, pos, side);
                }
                catch (final Exception e) {

                    Constants.LOG.warn("Issue with shouldSideBeRendered!", e);
                }
            }
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

		return true;
	}
	
//	@Override
//	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
//		final TileEntity tile = worldObj.getTileEntity(target.getBlockPos());
//		
//		if(tile instanceof TileElevator && !tile.isInvalid()){
//			final TileElevator tileElevator = (TileElevator) tile;
//			
//			if(tileElevator.heldState != null){
//				return ParticleUtils.spawnDigParticles(manager, tileElevator.heldState, worldObj, target.getBlockPos(), target.sideHit);
//			}
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
//		
//		final TileEntity tile = world.getTileEntity(pos);
//		
//		if(tile instanceof TileElevator && !tile.isInvalid()){
//			final TileElevator tileElevator = (TileElevator) tile;
//			
//			if(tileElevator.heldState != null){
//				return ParticleUtils.spawnBreakParticles(manager, tileElevator.heldState, world, pos);
//			}
//		}
//		
//		return false;
//	}
	
	
//	@Override
//	public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition,
//			IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
//		
//		final TileEntity tile = worldObj.getTileEntity(blockPosition);
//		
//		if(tile instanceof TileElevator && !tile.isInvalid()){
//			
//			final TileElevator tileElevator = (TileElevator) tile;
//			
//			if(tileElevator.heldState != null){
//				
//				worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY,entity.posZ, numberOfParticles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(tileElevator.heldState) });
//			}
//		}
//		
//		return false;
//	}
}
