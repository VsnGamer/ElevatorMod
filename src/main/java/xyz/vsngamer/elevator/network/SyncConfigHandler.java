package xyz.vsngamer.elevator.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.vsngamer.elevator.init.ModConfig;

public class SyncConfigHandler implements IMessageHandler<SyncConfig, IMessage> {

    @Override
    public IMessage onMessage(SyncConfig message, MessageContext ctx) {
        ModConfig.setClientConfig(message.getSameColor(), message.getRange(), message.getSkipUnreachable());
        return null;
    }
}
