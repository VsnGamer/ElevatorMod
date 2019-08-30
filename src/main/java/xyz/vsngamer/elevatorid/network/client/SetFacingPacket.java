package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;

import java.util.function.Supplier;

public class SetFacingPacket {

    private final Direction direction;
    private final BlockPos pos;

    public SetFacingPacket(Direction value, BlockPos pos) {
        this.direction = value;
        this.pos = pos;
    }

    public static void encode(SetFacingPacket msg, PacketBuffer buf) {
        buf.writeEnumValue(msg.direction);
        buf.writeBlockPos(msg.pos);
    }

    public static SetFacingPacket decode(PacketBuffer buf) {
        return new SetFacingPacket(buf.readEnumValue(Direction.class), buf.readBlockPos());
    }

    public static void handle(SetFacingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null)
            return;

        ServerWorld world = player.getServerWorld();
        BlockState state = world.getBlockState(msg.pos);
        if (state.getBlock() instanceof ElevatorBlock) {
            ctx.get().enqueueWork(() ->
                    world.setBlockState(msg.pos, state.with(ElevatorBlock.HORIZONTAL_FACING, msg.direction)));
            ctx.get().setPacketHandled(true);
        }
    }
}
