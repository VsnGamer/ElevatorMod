package xyz.vsngamer.elevatorid.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockReader;
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
    private DyeColor dyeColor;

    public ElevatorBlock(DyeColor color) {
        super(Block.Properties
                .create(Material.WOOL, color)
                .sound(SoundType.CLOTH)
                .hardnessAndResistance(0.8F));

        setRegistryName(ElevatorMod.ID, "elevator_" + color.getName());
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
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return true;
        }

        ItemStack handStack = player.getHeldItem(handIn);
        ElevatorTileEntity tile = getElevatorTile(worldIn, pos);
        if (tile == null) {
            return false;
        }

        if (isValidItem(handStack)) {
            Block handBlock = Block.getBlockFromItem(handStack.getItem());
            BlockState stateToApply = handBlock.getStateForPlacement(new FakeUseContext(player, handIn, hit));

            // Try set camo
            if (stateToApply != tile.getHeldState()) {
                setCamoAndUpdate(stateToApply, tile, worldIn);
                // If we successfully set camo, don't open the menu
                return true;
            }
        }

        // Remove camo
        if (player.isSneaking() && tile.getHeldState() != null) {
            NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(pos));
            return true;
        }

        NetworkHooks.openGui((ServerPlayerEntity) player, tile, pos);
        return true;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return ModConfig.GENERAL.mobSpawn.get() && super.canCreatureSpawn(state, world, pos, type, entityType);
    }

    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT_MIPPED; // Also render in MIPPED because of the arrow
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

    private ElevatorTileEntity getElevatorTile(World world, BlockPos pos) {
        // Get tile at pos
        TileEntity tile = world.getTileEntity(pos);

        // Check if it exists and is valid
        if (tile instanceof ElevatorTileEntity && !tile.isRemoved() && tile.getType().isValidBlock(world.getBlockState(pos).getBlock())) {
            return (ElevatorTileEntity) tile;
        }
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
