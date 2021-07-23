package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetDirectionalPacket {

    private final boolean value;
    private final BlockPos pos;

    public SetDirectionalPacket(boolean value, BlockPos pos) {
        this.value = value;
        this.pos = pos;
    }

    public static void encode(SetDirectionalPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetDirectionalPacket decode(FriendlyByteBuf buf) {
        return new SetDirectionalPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static void handle(SetDirectionalPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null)
                return;

            ServerLevel world = player.getLevel();
            if (!world.isLoaded(msg.pos))
                return;

            BlockState currState = world.getBlockState(msg.pos);

            if (currState.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, currState.setValue(ElevatorBlock.DIRECTIONAL, msg.value));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
