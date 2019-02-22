package xyz.vsngamer.elevatorid.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static {
        new CommonGeneral(BUILDER);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    static class CommonGeneral {
        final ForgeConfigSpec.BooleanValue Test;

        CommonGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            Test = builder.comment("say yay?").define("yay", true);
            builder.pop();
        }
    }

//    @Config.Comment("Can mobs spawn in elevators ?")
//    public static boolean mobSpawn = true;
//
//    @Config.Comment("Realign players after teleporting to the center of elevator ?")
//    public static boolean precisionTarget = true;
//
//    @Config.Comment("Should elevators have the same color to teleport ?")
//    public static boolean sameColor = true;
}
//TODO: This class
