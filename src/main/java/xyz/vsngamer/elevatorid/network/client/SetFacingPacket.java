package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

public record SetFacingPacket(Direction direction, BlockPos pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(ElevatorMod.ID, "set_facing");

    public SetFacingPacket(final FriendlyByteBuf buf) {
        this(buf.readEnum(Direction.class), buf.readBlockPos());
    }


    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(direction);
        buf.writeBlockPos(pos);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    public static class Handler {
        private static final Handler INSTANCE = new Handler();

        public static Handler getInstance() {
            return INSTANCE;
        }

        public void handle(SetFacingPacket msg, PlayPayloadContext ctx) {
            ctx.workHandler().submitAsync(() -> {
                Player player = ctx.player().orElse(null);
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
}
