package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetFacingPacket {

    private final Direction direction;
    private final BlockPos pos;

    public SetFacingPacket(Direction value, BlockPos pos) {
        this.direction = value;
        this.pos = pos;
    }

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
            if (player == null)
                return;

            ServerLevel world = player.getLevel();
            if (!world.isLoaded(msg.pos))
                return;

            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.FACING, msg.direction));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
