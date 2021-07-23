package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetArrowPacket {

    private final boolean value;
    private final BlockPos pos;

    public SetArrowPacket(boolean value, BlockPos pos) {
        this.value = value;
        this.pos = pos;
    }


    public static void encode(SetArrowPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetArrowPacket decode(FriendlyByteBuf buf) {
        return new SetArrowPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static void handle(SetArrowPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null)
                return;

            ServerLevel world = player.getLevel();
            if (!world.isLoaded(msg.pos))
                return;

            BlockState curState = world.getBlockState(msg.pos);
            if (curState.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, curState.setValue(ElevatorBlock.SHOW_ARROW, msg.value));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
