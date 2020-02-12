package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetDirectionalPacket {

    private final boolean value;
    private final BlockPos pos;

    public SetDirectionalPacket(boolean value, BlockPos pos) {
        this.value = value;
        this.pos = pos;
    }

    public static void encode(SetDirectionalPacket msg, PacketBuffer buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetDirectionalPacket decode(PacketBuffer buf) {
        return new SetDirectionalPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static boolean handle(SetDirectionalPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null)
                return;

            ServerWorld world = player.getServerWorld();
            BlockPos pos = msg.pos;
            BlockState currState = world.getBlockState(pos);

            if (currState.getBlock() instanceof ElevatorBlock) {
                world.setBlockState(pos, currState.with(ElevatorBlock.DIRECTIONAL, msg.value));
            }
        });

        return true;
    }
}
