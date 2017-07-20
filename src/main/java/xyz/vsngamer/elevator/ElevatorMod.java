package xyz.vsngamer.elevator;

import cofh.core.util.ConfigHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.vsngamer.elevator.init.ElevatorBlocks;
import xyz.vsngamer.elevator.proxy.CommonProxy;

@Mod(modid = Ref.MOD_ID,
        name = Ref.NAME,
        version = Ref.VERSION,
        acceptedMinecraftVersions = Ref.ACCPEPTED_VERSIONS,
        guiFactory = Ref.GUI_FACTORY,
        updateJSON = Ref.UPDATE_JSON)
public class ElevatorMod {

    public static final ConfigHandler CONFIG = new ConfigHandler(Ref.VERSION);
    public static final Logger LOG = LogManager.getLogger(Ref.MOD_ID);
    @Instance(Ref.MOD_ID)
    public static ElevatorMod instance;
    @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG.setConfiguration(new Configuration(event.getSuggestedConfigurationFile(), true));

        ElevatorBlocks.preInit();

        proxy.preInit(event);
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        proxy.initialize(event);
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CONFIG.cleanUp(false, true);
    }

/*
    @EventHandler
    public void onMissingMappingsI(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> missing : event.getMappings()) {
            EnumDyeColor color = null;
            if (missing.key.getResourceDomain().equals(Ref.MOD_ID + ":BlockElevator")) {
                color = EnumDyeColor.WHITE;
            } else if (missing.key.getResourceDomain().startsWith(Ref.MOD_ID + ":BlockElevator")) {
                String name = missing.key.getResourceDomain().substring((Ref.MOD_ID + ":BlockElevator").length());
                for (EnumDyeColor candidate : EnumDyeColor.values()) {
                    if (candidate.getUnlocalizedName().equalsIgnoreCase(name)) {
                        color = candidate;
                        break;
                    }
                }
            }
            if (color != null) {
                missing.remap(Registry.elevatorsItems.get(color));
                break;
            }
        }
    }

    @EventHandler
    public void onMissingMappingsB(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> missing : event.getMappings()) {
            EnumDyeColor color = null;
            if (missing.key.getResourceDomain().equals(Ref.MOD_ID + ":BlockElevator")) {
                color = EnumDyeColor.WHITE;
            } else if (missing.key.getResourceDomain().startsWith(Ref.MOD_ID + ":BlockElevator")) {
                String name = missing.key.getResourceDomain().substring((Ref.MOD_ID + ":BlockElevator").length());
                for (EnumDyeColor candidate : EnumDyeColor.values()) {
                    if (candidate.getUnlocalizedName().equalsIgnoreCase(name)) {
                        color = candidate;
                        break;
                    }
                }
            }
            if (color != null) {
                missing.remap(Registry.elevatorsBlocks.get(color));
                break;
            }
        }
    }*/
}
