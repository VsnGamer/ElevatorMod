package xyz.vsngamer.elevator.tile;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.darkhax.bookshelf.data.Blockstates;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.init.Registry;

@SideOnly(Side.CLIENT)
public class ModelCamoElevator implements IBakedModel {

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

		final Minecraft mc = Minecraft.getMinecraft();
		final BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

		final IBlockState heldState = ((IExtendedBlockState) state).getValue(Blockstates.HELD_STATE);
		final IBlockAccess heldWorld = ((IExtendedBlockState) state).getValue(Blockstates.BLOCK_ACCESS);
		final BlockPos heldPos = ((IExtendedBlockState) state).getValue(Blockstates.BLOCKPOS);

		if (!(state.getBlock() instanceof BlockElevator)) {
			return mc.getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel()
					.getQuads(state, side, rand);
		}

		for (EnumDyeColor color : EnumDyeColor.values()) {
			String regName = state.getBlock().getRegistryName().toString();
			if (heldState == null && layer == BlockRenderLayer.SOLID && regName.contains(color.toString().toLowerCase()) && regName.contains("elevatorid:elevator")) {
				IBakedModel model = RenderUtils.getBakedModel(new ItemStack(Registry.elevatorsBlocks.get(color)));
				return model.getQuads(state, side, rand);
			}
		}
		if (layer != null && heldState != null && heldState.getBlock().canRenderInLayer(heldState, layer)) {

			final IBlockState actualState = heldState.getBlock().getActualState(heldState,
					new ElevatorBlockAccess(heldWorld), heldPos);
			final IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes()
					.getModelForState(actualState);
			final IBlockState extended = heldState.getBlock().getExtendedState(actualState,
					new ElevatorBlockAccess(heldWorld), heldPos);
			return model.getQuads(extended, side, rand);
		}
		return ImmutableList.of();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {

		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {

		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/wool_colored_white");
	}

	@Override
	public ItemOverrideList getOverrides() {

		return ItemOverrideList.NONE;
	}

	private static class ElevatorBlockAccess implements IBlockAccess {

		private final IBlockAccess access;

		private ElevatorBlockAccess(IBlockAccess access) {

			this.access = access;
		}

		@Override
		public TileEntity getTileEntity(BlockPos pos) {

			return this.access.getTileEntity(pos);
		}

		@Override
		public int getCombinedLight(BlockPos pos, int light) {

			return 15 << 20 | 15 << 4;
		}

		@Override
		public IBlockState getBlockState(BlockPos pos) {

			IBlockState state = this.access.getBlockState(pos);

			if (state.getBlock() instanceof BlockElevator) {
				state = ((TileElevator) this.access.getTileEntity(pos)).heldState;
			}

			return state == null ? Blocks.AIR.getDefaultState() : state;
		}

		@Override
		public boolean isAirBlock(BlockPos pos) {

			return this.access.isAirBlock(pos);
		}

		@Override
		public int getStrongPower(BlockPos pos, EnumFacing direction) {

			return this.access.getStrongPower(pos, direction);
		}

		@Override
		public WorldType getWorldType() {

			return this.access.getWorldType();
		}

		@Override
		public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {

			return this.access.isSideSolid(pos, side, _default);
		}

		@Override
		public Biome getBiome(BlockPos pos) {

			return this.access.getBiome(pos);
		}
	}
}
