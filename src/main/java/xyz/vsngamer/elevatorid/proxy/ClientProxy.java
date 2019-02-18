package xyz.vsngamer.elevatorid.proxy;

import net.minecraftforge.common.MinecraftForge;
import xyz.vsngamer.elevatorid.ElevatorHandler;

public class ClientProxy{

    public void preInit() {
        //Registry.registerRenders();
        MinecraftForge.EVENT_BUS.register(new ElevatorHandler());
    }
}
