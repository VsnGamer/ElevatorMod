package xyz.vsngamer.elevatorid.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.ElevatorMod;

public record TeleportRequest(BlockPos from, BlockPos to) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(ElevatorMod.ID, "teleport_request");


    public TeleportRequest(final FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(from);
        buf.writeBlockPos(to);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
