package xyz.vsngamer.elevator.proxy;

import net.minecraftforge.common.MinecraftForge;
import xyz.vsngamer.elevator.ElevatorHandler;
import xyz.vsngamer.elevator.init.Registry;

public class ClientProxy{

	public void preInit() {
		MinecraftForge.EVENT_BUS.register(new ElevatorHandler());
	}
}
