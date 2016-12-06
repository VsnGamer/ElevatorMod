package xyz.vsngamer.elevator.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xyz.vsngamer.elevator.Ref;

public class NetworkHandler {
    public static SimpleNetworkWrapper networkWrapper;

    public static void init() {
        networkWrapper = new SimpleNetworkWrapper(Ref.MOD_ID);
        networkWrapper.registerMessage(TeleportHandler.class, TeleportRequest.class, 0, Side.SERVER);
    }
}
