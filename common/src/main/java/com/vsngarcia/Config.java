package com.vsngarcia;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final CommonGeneral GENERAL = new CommonGeneral(BUILDER);
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static class CommonGeneral {
        public final ModConfigSpec.BooleanValue sameColor;
        public final ModConfigSpec.IntValue range;
        public final ModConfigSpec.IntValue activationRange;
        public final ModConfigSpec.BooleanValue precisionTarget;
        public final ModConfigSpec.BooleanValue mobSpawn;
        public final ModConfigSpec.BooleanValue resetPitchNormal;
        public final ModConfigSpec.BooleanValue resetPitchDirectional;
        public final ModConfigSpec.BooleanValue useXP;
        public final ModConfigSpec.IntValue XPPointsAmount;

        CommonGeneral(ModConfigSpec.Builder builder) {
            builder.push("General");

            sameColor = builder
                    .comment("Should elevators have the same color in order to teleport ?")
                    .define("sameColor", false);

            range = builder
                    .comment("Elevator range")
                    .defineInRange("range", 384, 3, 4064);

            activationRange = builder
                    .comment("Elevator activation range")
                    .defineInRange("activationRange", 6, 1, 64);

            precisionTarget = builder
                    .comment("Realign players to the center of elevator ?")
                    .define("precisionTarget", true);

            mobSpawn = builder
                    .worldRestart()
                    .comment("Can mobs spawn on elevators ?")
                    .define("mobSpawn", false);

            resetPitchNormal = builder
                    .comment("Reset pitch to 0 when teleporting to normal elevators ?")
                    .define("resetPitchNormal", false);

            resetPitchDirectional = builder
                    .comment("Reset pitch to 0 when teleporting to directional elevators ?")
                    .define("resetPitchDirectional", true);

            useXP = builder
                    .comment("Should teleporting require XP ?")
                    .define("useXP", false);

            XPPointsAmount = builder
                    .comment("Amount of XP points to use when useXP is enabled", "Note this is NOT experience levels")
                    .defineInRange("XPPointsAmount", 1, 1, Integer.MAX_VALUE);

            builder.pop();
        }
    }
}
