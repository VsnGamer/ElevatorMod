package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

import java.util.function.Supplier;

public record SetFacingPacket(Direction direction, BlockPos pos) {
    public static void encode(SetFacingPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.direction);
        buf.writeBlockPos(msg.pos);
    }

    public static SetFacingPacket decode(FriendlyByteBuf buf) {
        return new SetFacingPacket(buf.readEnum(Direction.class), buf.readBlockPos());
    }

    public static void handle(SetFacingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (NetworkHandler.isBadPacket(player, msg.pos))
                return;

            ServerLevel world = player.getLevel();
            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.FACING, msg.direction));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
