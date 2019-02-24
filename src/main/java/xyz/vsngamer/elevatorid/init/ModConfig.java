package xyz.vsngamer.elevatorid.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final CommonGeneral GENERAL = new CommonGeneral(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class CommonGeneral {
        public final ForgeConfigSpec.BooleanValue sameColor;
        public final ForgeConfigSpec.BooleanValue precisionTarget;
        public final ForgeConfigSpec.BooleanValue mobSpawn;

        CommonGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            sameColor = builder.comment("Should elevators have the same color in order to teleport ?").define("sameColor", false);
            precisionTarget = builder.comment("Realign players to the center of elevator ?").define("precisionTarget", true);
            mobSpawn = builder.comment("Can mobs spawn on elevators ?").define("mobSpawn", false);

            builder.pop();
        }
    }
}
