package xyz.vsngamer.elevatorid.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static final SoundEvent TELEPORT = createSound("teleport");

    private static SoundEvent createSound(String id){
        ResourceLocation loc = new ResourceLocation(ElevatorMod.ID, id);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(TELEPORT);
    }
}
