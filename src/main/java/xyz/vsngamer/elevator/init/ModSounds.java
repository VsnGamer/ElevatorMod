package xyz.vsngamer.elevator.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.vsngamer.elevator.Ref;

 @Mod.EventBusSubscriber(modid = Ref.MOD_ID)
public class ModSounds {

	public static SoundEvent teleport;
	
	@SubscribeEvent
	public static void registerSound(RegistryEvent.Register<SoundEvent> e) {
		final ResourceLocation loc = new ResourceLocation(Ref.MOD_ID,"teleport");
		teleport = new SoundEvent(loc).setRegistryName(loc);
		e.getRegistry().register(teleport);
	}

}
