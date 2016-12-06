package xyz.vsngamer.elevator.proxy;

import net.minecraftforge.common.MinecraftForge;
import xyz.vsngamer.elevator.ElevatorHandler;
import xyz.vsngamer.elevator.init.ModItems;

public class ClientProxy implements CommonProxy {

    @Override
    public void preInit() {
        ModItems.registerRenders();

        MinecraftForge.EVENT_BUS.register(new ElevatorHandler());
    }

}
