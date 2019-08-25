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

    private ElevatorTileEntity elevatorTile;

    ElevatorContainer(int id, BlockPos pos, PlayerEntity player) {
        super(ELEVATOR_CONTAINER, id);

        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof ElevatorTileEntity)
            elevatorTile = (ElevatorTileEntity) tile;

    }

    // This will probably not be necessary
//    private void addInventorySlots() {
//        // Inventory
//        for (int l = 0; l < 3; ++l) {
//            for (int k = 0; k < 9; ++k) {
//                this.addSlot(new Slot(inventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
//            }
//        }
//
//        // Hotbar
//        for (int i1 = 0; i1 < 9; ++i1) {
//            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 109));
//        }
//    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.world, elevatorTile.getPos()), playerIn, elevatorTile.getBlockState().getBlock());
    }

    public ElevatorTileEntity getTile() {
        return elevatorTile;
    }

    public static ContainerType<ElevatorContainer> buildContainerType() {
        ContainerType<ElevatorContainer> type = IForgeContainerType.create((windowId, inv, data) ->
                new ElevatorContainer(windowId, data.readBlockPos(), Minecraft.getInstance().player));
        type.setRegistryName("elevator_container");
        return type;
    }
}
