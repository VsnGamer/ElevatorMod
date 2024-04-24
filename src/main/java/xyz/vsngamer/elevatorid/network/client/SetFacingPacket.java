package xyz.vsngamer.elevatorid.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

public record SetFacingPacket(Direction direction, BlockPos pos) implements CustomPacketPayload {
    public static final Type<SetFacingPacket> TYPE = new Type<>(new ResourceLocation(ElevatorMod.ID, "set_facing"));

    public static final StreamCodec<ByteBuf, SetFacingPacket> STREAM_CODEC = StreamCodec.composite(
            Direction.STREAM_CODEC,
            SetFacingPacket::direction,
            BlockPos.STREAM_CODEC,
            SetFacingPacket::pos,
            SetFacingPacket::new
    );

    @Override
    public Type<SetFacingPacket> type() {
        return TYPE;
    }

    public static void handle(SetFacingPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (NetworkHandler.isBadClientPacket(player, msg.pos))
                return;

            Level world = player.level();
            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.FACING, msg.direction));
            }
        });
    }
}
