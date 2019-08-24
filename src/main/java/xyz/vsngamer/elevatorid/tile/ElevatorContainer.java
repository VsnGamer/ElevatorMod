package xyz.vsngamer.elevatorid.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;

import javax.annotation.Nonnull;

import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_CONTAINER;

public class ElevatorContainer extends Container {

    private final TileEntity tile;

    private ElevatorContainer(int id, BlockPos pos, PlayerEntity player) {
        super(ELEVATOR_CONTAINER, id);
        tile = player.world.getTileEntity(pos);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.world, tile.getPos()), playerIn, tile.getBlockState().getBlock());
    }

    public static ContainerType<ElevatorContainer> buildContainerType() {
        ContainerType<ElevatorContainer> type = IForgeContainerType.create((windowId, inv, data) ->
                new ElevatorContainer(windowId, data.readBlockPos(), Minecraft.getInstance().player));
        type.setRegistryName("elevator_container");
        return type;
    }
}
