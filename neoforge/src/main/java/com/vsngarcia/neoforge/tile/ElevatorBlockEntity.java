package com.vsngarcia.neoforge.tile;

import com.vsngarcia.level.ElevatorBlockEntityBase;
import com.vsngarcia.level.ElevatorContainer;
import com.vsngarcia.neoforge.client.render.ElevatorBakedModel;
import com.vsngarcia.neoforge.init.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.model.data.ModelData;

public class ElevatorBlockEntity extends ElevatorBlockEntityBase {
    public ElevatorBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.ELEVATOR_TILE_ENTITY.get(), pos, state);
    }

    @Override
    protected SoundEvent camouflageSound() {
        return Registry.CAMOUFLAGE_SOUND.get();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ElevatorContainer(Registry.ELEVATOR_CONTAINER.get(), id, worldPosition, player);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(ElevatorBakedModel.HELD_STATE, heldState).build();
    }

    @Override
    public void setChanged() {
        super.setChanged();

        requestModelDataUpdate();

        var auxLightManager = level != null ? level.getAuxLightManager(worldPosition) : null;
        if (auxLightManager == null) {
            return;
        }

        auxLightManager.setLightAt(
                worldPosition,
                heldState != null ? heldState.getLightEmission(level, worldPosition) : 0
        );
    }

    @Override
    public void handleUpdateTag(ValueInput in) {
        super.handleUpdateTag(in);
        setChanged();
    }

    @Override
    public void onDataPacket(Connection net, ValueInput in) {
        handleUpdateTag(in);
    }
}
