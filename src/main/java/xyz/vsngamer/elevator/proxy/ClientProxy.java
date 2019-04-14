package xyz.vsngamer.elevator.proxy;

import net.minecraft.client.Minecraft;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.init.Registry;
import xyz.vsngamer.elevator.render.ColorCamoElevator;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        for (BlockElevator elevator : Registry.elevatorsBlocks.values()) {
            Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ColorCamoElevator(), elevator);
        }
    }
}
