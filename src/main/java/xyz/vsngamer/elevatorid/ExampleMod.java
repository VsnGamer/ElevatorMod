//package xyz.vsngamer.elevatorid;
//
//import net.minecraft.block.Block;
//import net.minecraft.init.Blocks;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
////@Mod("elevatorid")
//public class ExampleMod {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    public ExampleMod() {
//        // Register the setup method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        // Register the doClientStuff method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
//
//        // Register ourselves for server and other game events we are interested in
//        MinecraftForge.EVENT_BUS.register(this);
//    }
//
//    private void setup(final FMLCommonSetupEvent event) {
//        // some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
//    }
//
//    private void doClientStuff(final FMLClientSetupEvent event) {
//        // do something that can only be done on the client
//        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
//    }
//
//    @SubscribeEvent
//    public void onServerStarting(FMLServerStartingEvent event) {
//        // do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }
//
//    //Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }
//}
