package xyz.vsngamer.elevator.init;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Red;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.vsngamer.elevator.Ref;

public class ModSounds {

	public static SoundEvent teleport;

	public static void registerSounds() {

		teleport = registerSound("teleport");
	}

	private static SoundEvent registerSound(String soundName) {
		ResourceLocation loc = new ResourceLocation(Ref.MOD_ID ,soundName);
		SoundEvent e = new SoundEvent(loc);
		return GameRegistry.register(e, loc);
		
	}
}
