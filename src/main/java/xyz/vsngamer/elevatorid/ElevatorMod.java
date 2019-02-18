package xyz.vsngamer.elevatorid;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.proxy.ClientProxy;

@Mod(Ref.MOD_ID)
public class ElevatorMod {
    //TODO @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.SERVER_PROXY_CLASS)
    //@OnlyIn(Dist.CLIENT)
    //private static ClientProxy proxy = new ClientProxy();


    public ElevatorMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final ItemGroup CREATIVE_TAB = new ElevatorModTab();

    private void commonSetup(final FMLCommonSetupEvent event) {
        //proxy.preInit();
        NetworkHandler.init();
    }

    private void clientInit(final FMLClientSetupEvent event){
        //MinecraftForge.EVENT_BUS.register(new ElevatorHandler());
    }
}
