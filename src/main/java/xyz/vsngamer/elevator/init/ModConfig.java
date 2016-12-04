package xyz.vsngamer.elevator.init;

import java.io.File;

import com.ibm.icu.util.ULocale.Category;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.vsngamer.elevator.Ref;

public class ModConfig {

	public static Configuration config;

	public static boolean mobSpawn;
	public static boolean precisionTarget;

	@SubscribeEvent
	public void configChanged(ConfigChangedEvent event){
		if(event.getModID().equals(Ref.MOD_ID)){
			syncConfig();
		}
	}
	
	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	private static void syncConfig() {
		
		String category = "General";
		
		mobSpawn = config.getBoolean("mobSpawn", category, true, "Can mobs spawn in elevators ?");
		precisionTarget = config.getBoolean("precisionTarget", category, true, "Realign players after teleporting to the center of elevator ?");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
