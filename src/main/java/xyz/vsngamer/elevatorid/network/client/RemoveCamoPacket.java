package xyz.vsngamer.elevatorid.network.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

public record RemoveCamoPacket(BlockPos pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(ElevatorMod.ID, "remove_camo");

    public RemoveCamoPacket(final FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }


    @Override
    public void write(FriendlyByteBuf buf) {
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

        public void handle(RemoveCamoPacket msg, PlayPayloadContext ctx) {
            ctx.workHandler().submitAsync(() -> {
                Player player = ctx.player().orElse(null);
                if (NetworkHandler.isBadClientPacket(player, msg.pos)) return;

                if (player.level().getBlockEntity(msg.pos) instanceof ElevatorTileEntity tile) {
                    tile.setCamoAndUpdate(null);
                }
            });
        }
    }
}
