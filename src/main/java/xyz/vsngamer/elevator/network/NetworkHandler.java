package xyz.vsngamer.elevator.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xyz.vsngamer.elevator.Ref;

public class NetworkHandler {
    public static SimpleNetworkWrapper networkWrapper = new SimpleNetworkWrapper(Ref.MOD_ID);

    public static void init() {
        networkWrapper.registerMessage(TeleportHandler.class, TeleportRequest.class, 0, Side.SERVER);
    }
}
