package xyz.vsngamer.elevatorid;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod(ElevatorMod.ID)
public class ElevatorMod {
    public static final String ID = "elevatorid";

    public ElevatorMod(IEventBus eventBus) {
        Registry.init(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, xyz.vsngamer.elevatorid.init.ModConfig.SPEC);
    }
}
