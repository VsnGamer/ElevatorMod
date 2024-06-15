package xyz.vsngamer.elevatorid.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
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

public record SetDirectionalPacket(boolean value, BlockPos pos) implements CustomPacketPayload {
    public static final Type<SetDirectionalPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ElevatorMod.ID, "set_directional"));

    public static final StreamCodec<ByteBuf, SetDirectionalPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SetDirectionalPacket::value,
            BlockPos.STREAM_CODEC,
            SetDirectionalPacket::pos,
            SetDirectionalPacket::new
    );

    @Override
    public Type<SetDirectionalPacket> type() {
        return TYPE;
    }


    public static void handle(SetDirectionalPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (NetworkHandler.isBadClientPacket(player, msg.pos))
                return;

            Level world = player.level();
            BlockState state = world.getBlockState(msg.pos);
            if (state.getBlock() instanceof ElevatorBlock) {
                world.setBlockAndUpdate(msg.pos, state.setValue(ElevatorBlock.DIRECTIONAL, msg.value));
            }
        });
    }
}

