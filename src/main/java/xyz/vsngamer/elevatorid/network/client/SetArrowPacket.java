package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

import java.util.function.Supplier;

public record SetArrowPacket(boolean value, BlockPos pos) {
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
            if (NetworkHandler.isBadClientPacket(player, msg.pos))
                return;

            ServerLevel world = player.getLevel();
            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.SHOW_ARROW, msg.value));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
