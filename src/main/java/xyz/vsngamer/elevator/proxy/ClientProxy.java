package xyz.vsngamer.elevator.proxy;

import net.minecraftforge.common.MinecraftForge;
import xyz.vsngamer.elevator.ElevatorHandler;
import xyz.vsngamer.elevator.init.Registry;

public class ClientProxy implements CommonProxy {

	@Override
	public void preInit() {
		//Registry.registerRenders();
		MinecraftForge.EVENT_BUS.register(new ElevatorHandler());
	}
}
