package xyz.vsngamer.elevatorid;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod(ElevatorMod.ID)
public class ElevatorMod {
    public static final String ID = "elevatorid";

    public ElevatorMod(IEventBus eventBus, ModContainer container) {
        Registry.init(eventBus);

        container.registerConfig(ModConfig.Type.SERVER, xyz.vsngamer.elevatorid.init.ModConfig.SPEC);
    }
}
