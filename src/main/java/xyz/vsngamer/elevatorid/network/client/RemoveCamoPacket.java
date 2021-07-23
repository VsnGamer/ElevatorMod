package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.function.Supplier;

public class RemoveCamoPacket {

    private final BlockPos pos;

    public RemoveCamoPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(RemoveCamoPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RemoveCamoPacket decode(FriendlyByteBuf buf) {
        return new RemoveCamoPacket(buf.readBlockPos());
    }

    public static void handle(RemoveCamoPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null)
                return;

            ServerLevel world = player.getLevel();
            if (!world.isLoaded(msg.pos))
                return;

            BlockEntity tile = world.getBlockEntity(msg.pos);
            if (tile instanceof ElevatorTileEntity) {
                ((ElevatorTileEntity) tile).setHeldState(null);
                world.playSound(null, msg.pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1F, 1F);
            }

        });

        ctx.get().setPacketHandled(true);
    }
}
