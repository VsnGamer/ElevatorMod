package xyz.vsngamer.elevatorid.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;

import javax.annotation.Nonnull;

import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_CONTAINER;

public class ElevatorContainer extends Container {

    private final TileEntity tile;
    private final PlayerInventory inventory;

    ElevatorContainer(int id, BlockPos pos, PlayerEntity player, PlayerInventory inv) {
        super(ELEVATOR_CONTAINER, id);
        tile = player.world.getTileEntity(pos);
        inventory = inv;

        addInventorySlots();
    }

    // This will probably not be necessary
    private void addInventorySlots() {
        // Inventory
        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        // Hotbar
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 109));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.world, tile.getPos()), playerIn, tile.getBlockState().getBlock());
    }

    public TileEntity getTile() {
        return tile;
    }

    public static ContainerType<ElevatorContainer> buildContainerType() {
        ContainerType<ElevatorContainer> type = IForgeContainerType.create((windowId, inv, data) ->
                new ElevatorContainer(windowId, data.readBlockPos(), Minecraft.getInstance().player, inv));
        type.setRegistryName("elevator_container");
        return type;
    }
}
