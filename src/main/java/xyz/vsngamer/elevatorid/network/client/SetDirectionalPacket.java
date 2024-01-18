package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.network.CustomPayloadEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

public record SetDirectionalPacket(boolean value, BlockPos pos) {
    public static void encode(SetDirectionalPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static SetDirectionalPacket decode(FriendlyByteBuf buf) {
        return new SetDirectionalPacket(buf.readBoolean(), buf.readBlockPos());
    }

    public static void handle(SetDirectionalPacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (NetworkHandler.isBadClientPacket(player, msg.pos))
                return;

            Level world = player.level();
            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.DIRECTIONAL, msg.value));
            }
        });

        ctx.setPacketHandled(true);
    }
}
