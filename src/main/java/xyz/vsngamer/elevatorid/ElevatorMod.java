package xyz.vsngamer.elevatorid;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.vsngamer.elevatorid.network.NetworkHandler;

@Mod(Ref.MOD_ID)
public class ElevatorMod {
    //@SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.SERVER_PROXY_CLASS)
    //@OnlyIn(Dist.CLIENT)
    //private static ClientProxy proxy = new ClientProxy();

    public ElevatorMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, xyz.vsngamer.elevatorid.init.ModConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final ItemGroup CREATIVE_TAB = new ElevatorModTab();

    private void commonSetup(final FMLCommonSetupEvent event) {
        //proxy.preInit();
        NetworkHandler.init();
    }
}
