package xyz.vsngamer.elevatorid.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_TILE_ENTITY;

public class ElevatorTileEntity extends TileEntity implements INamedContainerProvider {

    public ElevatorTileEntity() {
        super(ELEVATOR_TILE_ENTITY);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.elevatorid.elevator");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new ElevatorContainer(id, pos, player);
    }

    public static TileEntityType<ElevatorTileEntity> buildTileType(Block... validBlocks) {
        TileEntityType<ElevatorTileEntity> type = TileEntityType.Builder.create(ElevatorTileEntity::new, validBlocks).build(null);
        type.setRegistryName("elevator_tile");
        return type;
    }
}
