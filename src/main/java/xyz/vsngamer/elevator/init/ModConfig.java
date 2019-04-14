package xyz.vsngamer.elevator.init;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.vsngamer.elevator.Ref;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID)
@Config(modid = Ref.MOD_ID)
public class ModConfig {
    @Config.Comment("Elevators only work with the same color?")
    public static boolean sameColor = false;

    @Config.Comment("Realign players to the center of elevator after teleporting ?")
    public static boolean precisionTarget = true;

    @Config.Comment("Can mobs spawn on elevators ?")
    public static boolean mobSpawn = false;

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Ref.MOD_ID)) {
            ConfigManager.sync(Ref.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
