package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.function.Supplier;

public class RemoveCamoPacket {

    private final BlockPos pos;

    public RemoveCamoPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RemoveCamoPacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RemoveCamoPacket decode(PacketBuffer buf) {
        return new RemoveCamoPacket(buf.readBlockPos());
    }

    public static boolean handle(RemoveCamoPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;

            ServerWorld world = player.getServerWorld();
            TileEntity tile = world.getTileEntity(msg.pos);
            if (tile instanceof ElevatorTileEntity) {
                ((ElevatorTileEntity) tile).setHeldState(null);
                world.playSound(null, msg.pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1F, 1F);
            }
        });

        return true;
    }
}
