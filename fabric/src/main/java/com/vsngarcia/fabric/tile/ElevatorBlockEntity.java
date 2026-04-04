package com.vsngarcia.fabric.tile;

import com.vsngarcia.fabric.ElevatorBlock;
import com.vsngarcia.fabric.FabricRegistry;
import com.vsngarcia.level.ElevatorBlockEntityBase;
import com.vsngarcia.level.ElevatorContainer;
import net.fabricmc.fabric.api.blockgetter.v2.RenderDataBlockEntity;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class ElevatorBlockEntity extends ElevatorBlockEntityBase implements RenderDataBlockEntity, ExtendedMenuProvider<FabricRegistry.ElevatorContainerData> {
    public ElevatorBlockEntity(BlockPos pos, BlockState state) {
        super(FabricRegistry.ELEVATOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected SoundEvent camouflageSound() {
        return FabricRegistry.CAMOUFLAGE_SOUND;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElevatorContainer(FabricRegistry.ELEVATOR_CONTAINER, id, worldPosition, player);
    }

    @Override
    public @Nullable Object getRenderData() {
        return heldState;
    }

    @Override
    public FabricRegistry.ElevatorContainerData getScreenOpeningData(ServerPlayer player) {
        return new FabricRegistry.ElevatorContainerData(worldPosition);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level != null) {
            level.setBlock(
                    worldPosition,
                    getBlockState().setValue(ElevatorBlock.LIGHT, heldState != null ? heldState.getLightEmission() : 0),
                    ElevatorBlock.UPDATE_ALL
            );
        }
    }
}
