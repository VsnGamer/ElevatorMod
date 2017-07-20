package xyz.vsngamer.elevator;

import cofh.core.util.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import xyz.vsngamer.elevator.init.ElevatorBlocks;
import xyz.vsngamer.elevator.proxy.CommonProxy;

@Mod(modid = Ref.MOD_ID,
        name = Ref.NAME,
        version = Ref.VERSION,
        acceptedMinecraftVersions = Ref.ACCPEPTED_VERSIONS,
        guiFactory = Ref.GUI_FACTORY,
        updateJSON = Ref.UPDATE_JSON)
@Mod.EventBusSubscriber
public class ElevatorMod {

    public static final ConfigHandler CONFIG = new ConfigHandler(Ref.VERSION);
    public static Logger LOG;
    @Instance(Ref.MOD_ID)
    public static ElevatorMod instance;
    @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @SubscribeEvent
    public static void onMissingMappingsItem(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> missing : event.getMappings()) {
            ResourceLocation res = missing.key;
            LOG.error("Missing item mapping: " + res);

            if (res.getResourceDomain().equals("elevatorid")) {
                String path = res.getResourcePath();

                if (path.startsWith("BlockElevatorwhite")) {
                    missing.remap(Item.getItemFromBlock(ElevatorBlocks.elevator));
                } else if (path.startsWith("elevator_white")) {
                    missing.remap(Item.getItemFromBlock(ElevatorBlocks.elevator));
                } else {
                    missing.ignore();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMissingMappingsBlock(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> missing : event.getMappings()) {
            ResourceLocation res = missing.key;
            LOG.error("Missing block mapping: " + res);

            if (res.getResourceDomain().equals("elevatorid")) {
                String path = res.getResourcePath();

                if (path.startsWith("BlockElevatorwhite")) {
                    missing.remap(ElevatorBlocks.elevator);
                } else if (path.startsWith("elevator_white")) {
                    missing.remap(ElevatorBlocks.elevator);
                } else {
                    missing.ignore();
                }
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().getDataFixer().init(Ref.MOD_ID, 0);
        LOG = event.getModLog();
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
}
