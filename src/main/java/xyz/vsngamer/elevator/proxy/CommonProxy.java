package xyz.vsngamer.elevator.proxy;

import cofh.core.render.IModelRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.vsngamer.elevator.init.ElevatorConfig;
import xyz.vsngamer.elevator.network.NetworkHandler;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ElevatorConfig.syncConfigCommon();
        NetworkHandler.init();
    }

    public void initialize(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return false;
    }
}
