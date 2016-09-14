package xyz.vsngamer.elevator.proxy;

import xyz.vsngamer.elevator.init.ModBlocks;

public class ClientProxy implements CommonProxy{

	@Override
	public void preInit() {
		ModBlocks.registerRenders();

	}

}
