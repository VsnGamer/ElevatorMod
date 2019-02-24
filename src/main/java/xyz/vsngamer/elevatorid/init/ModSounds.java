package xyz.vsngamer.elevatorid.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.Ref;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {
    public static SoundEvent teleport;

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> e) {
        final ResourceLocation loc = new ResourceLocation(Ref.MOD_ID, "teleport");
        teleport = new SoundEvent(loc).setRegistryName(loc);
        e.getRegistry().register(teleport);
    }
}
