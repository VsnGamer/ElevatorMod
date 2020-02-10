package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetArrowPacket {

    private boolean value;
    private BlockPos pos;

    public SetArrowPacket(boolean value, BlockPos pos) {
        this.value = value;
        this.pos = pos;
    }


    public static void encode(SetArrowPacket msg, PacketBuffer buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetArrowPacket decode(PacketBuffer buf) {
        return new SetArrowPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static boolean handle(SetArrowPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null)
            return true;

        ServerWorld world = player.getServerWorld();
        BlockState curState = world.getBlockState(msg.pos);
        if (curState.getBlock() instanceof ElevatorBlock) {
            ctx.get().enqueueWork(() ->
                    world.setBlockState(msg.pos, curState.with(ElevatorBlock.SHOW_ARROW, msg.value)));
        }

        return true;
    }
}
