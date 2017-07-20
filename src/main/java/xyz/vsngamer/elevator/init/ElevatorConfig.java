package xyz.vsngamer.elevator.init;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;

import java.util.Objects;

/**
 * @author Mitchell Skaggs
 */
@Mod.EventBusSubscriber(value = {Side.CLIENT}, modid = Ref.MOD_ID)
public class ElevatorConfig {

    public static boolean canMobsSpawn;
    public static boolean realignTeleportingPlayers;
    public static boolean restrictTeleportToSameColor;

    public static void syncConfigCommon() {
        canMobsSpawn = ElevatorMod.CONFIG.get(
                Configuration.CATEGORY_GENERAL,
                "canMobsSpawn",
                false,
                "Can mobs spawn on elevators?");

        realignTeleportingPlayers = ElevatorMod.CONFIG.get(
                Configuration.CATEGORY_GENERAL,
                "realignTeleportingPlayers",
                true,
                "Realign players after teleporting to the center of elevator?");

        restrictTeleportToSameColor = ElevatorMod.CONFIG.get(
                Configuration.CATEGORY_GENERAL,
                "restrictTeleportToSameColor",
                true,
                "Restrict teleports to only lead to the same color?");

        ElevatorMod.CONFIG.save();
    }

    @SubscribeEvent
    public static void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Objects.equals(Ref.MOD_ID, event.getModID())) {
            syncConfigCommon();
        }
    }

}
