package xyz.vsngamer.elevatorid.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import xyz.vsngamer.elevatorid.ElevatorMod;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final CommonGeneral GENERAL = new CommonGeneral(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class CommonGeneral {
        public final ForgeConfigSpec.BooleanValue sameColor;
        public final ForgeConfigSpec.BooleanValue precisionTarget;
        public final ForgeConfigSpec.BooleanValue mobSpawn;
        public final ForgeConfigSpec.BooleanValue resetPitchNormal;
        public final ForgeConfigSpec.BooleanValue resetPitchDirectional;
        public final ForgeConfigSpec.IntValue range;

        CommonGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            sameColor = builder.worldRestart().comment("Should elevators have the same color in order to teleport ?").define("sameColor", false);
            range = builder.worldRestart().comment("Elevator range").defineInRange("range", 256, 3, 256);
            precisionTarget = builder.comment("Realign players to the center of elevator ?").define("precisionTarget", true);
            mobSpawn = builder.comment("Can mobs spawn on elevators ?").define("mobSpawn", false);
            resetPitchNormal = builder.comment("Reset pitch to 0 when teleporting to normal elevators ?").define("resetPitchNormal", false);
            resetPitchDirectional = builder.comment("Reset pitch to 0 when teleporting to directional elevators ?").define("resetPitchDirectional", true);

            builder.pop();
        }
    }

    @SubscribeEvent
    public static void onLoad(final net.minecraftforge.fml.config.ModConfig.Loading configEvent) {
        LogManager.getLogger().debug(FORGEMOD, "Loaded elevator config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final net.minecraftforge.fml.config.ModConfig.ConfigReloading configEvent) {
        LogManager.getLogger().fatal(FORGEMOD, "Elevator config just got changed on the file system!");
    }
}
