package xyz.vsngamer.elevatorid.init;

import net.minecraftforge.common.ForgeConfigSpec;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;

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

        CommonGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            sameColor = builder.comment("Should elevators have the same color in order to teleport ?").define("sameColor", false);
            precisionTarget = builder.comment("Realign players to the center of elevator ?").define("precisionTarget", true);
            mobSpawn = builder.comment("Can mobs spawn on elevators ?").define("mobSpawn", false);
            resetPitchNormal = builder.comment("Reset pitch to 0 when teleporting to normal elevators ?").define("resetPitchNormal", false);
            resetPitchDirectional = builder.comment("Reset pitch to 0 when teleporting to directional elevators ?").define("resetPitchDirectional", true);

            builder.pop();
        }
    }
}
