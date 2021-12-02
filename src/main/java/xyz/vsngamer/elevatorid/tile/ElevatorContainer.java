package xyz.vsngamer.elevatorid.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;

import javax.annotation.Nonnull;

import static xyz.vsngamer.elevatorid.init.Registry.ELEVATOR_CONTAINER;

public class ElevatorContainer extends AbstractContainerMenu {

    private final Direction playerFacing;
    private ElevatorTileEntity elevatorTile;
    private final BlockPos pos;

    ElevatorContainer(int id, BlockPos pos, Player player) {
        super(ELEVATOR_CONTAINER, id);

        BlockEntity tile = player.level.getBlockEntity(pos);
        if (tile instanceof ElevatorTileEntity)
            elevatorTile = (ElevatorTileEntity) tile;

        playerFacing = player.getDirection();
        this.pos = pos;
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return stillValid(ContainerLevelAccess.create(playerIn.level, elevatorTile.getBlockPos()), playerIn, elevatorTile.getBlockState().getBlock());
    }

    public BlockPos getPos() {
        return pos;
    }

    public ElevatorTileEntity getTile() {
        return elevatorTile;
    }

    public Direction getPlayerFacing() {
        return playerFacing;
    }

    public static MenuType<ElevatorContainer> buildContainerType() {
        MenuType<ElevatorContainer> type = IForgeMenuType.create((windowId, inv, data) ->
                new ElevatorContainer(windowId, data.readBlockPos(), inv.player));
        type.setRegistryName("elevator_container");
        return type;
    }
}
