package xyz.vsngamer.elevator.init;

import net.minecraftforge.common.config.Config;
import xyz.vsngamer.elevator.Ref;

@Config(modid = Ref.MOD_ID)
public class ModConfig {

    @Config.Name("general")
    public static final Options serverConfig = new Options();
    private static final Options clientConfig = new Options();

    public static class Options {
        @Config.Comment("Can mobs spawn in elevators ?")
        public boolean mobSpawn = false;

        @Config.Comment("Realign players after teleporting to the center of elevator ?")
        public boolean precisionTarget = true;

        @Config.Comment("Should elevators have the same color to teleport ?")
        public boolean sameColor = false;

        @Config.Comment("Maximum elevator range")
        @Config.RangeInt(min = 3)
        public int range = 256;
        
        @Config.Comment("Should teleport skip unreachable elevators ?")
        public boolean skipUnreachable = false;
    }

    public static Options getClientConfig() {
        return clientConfig;
    }

    public static void setClientConfig(boolean sameColor, int range, boolean skipUnreachable){
        clientConfig.sameColor = sameColor;
        clientConfig.range = range;
        clientConfig.skipUnreachable = skipUnreachable;
    }
}
