package xyz.vsngamer.elevator.init;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.vsngamer.elevator.Ref;

import java.io.File;

@Config(modid = Ref.MOD_ID)
public class ModConfig {

    @Config.Comment("Can mobs spawn in elevators ?")
    public static boolean mobSpawn = true;

    @Config.Comment("Realign players after teleporting to the center of elevator ?")
    public static boolean precisionTarget = true;

    @Config.Comment("Should elevators have the same color to teleport ?")
    public static boolean sameColor = true;
}
