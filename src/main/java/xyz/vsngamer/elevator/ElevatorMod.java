package xyz.vsngamer.elevator;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.vsngamer.elevator.init.ModBlocks;
import xyz.vsngamer.elevator.init.ModConfig;
import xyz.vsngamer.elevator.init.ModItems;
import xyz.vsngamer.elevator.init.ModSounds;
import xyz.vsngamer.elevator.network.NetworkHandler;
import xyz.vsngamer.elevator.proxy.CommonProxy;

@Mod(modid = Ref.MOD_ID, name = Ref.NAME, version = Ref.VERSION, acceptedMinecraftVersions = Ref.ACCPEPTED_VERSIONS, guiFactory = Ref.GUI_FACTORY, updateJSON = Ref.UPDATE_JSON)
public class ElevatorMod {

	@Instance
	public static ElevatorMod instance;

	@SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final CreativeTabs CREATIVE_TAB = new ElevatorModTab();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
		proxy.preInit();
		ModSounds.registerSounds();

		NetworkHandler.init();

		File configDir = new File(event.getModConfigurationDirectory(), "ElevatorMod");
		ModConfig.init(new File(configDir, "ElevatorMod.cfg"));

		MinecraftForge.EVENT_BUS.register(new ModConfig());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {}

	@EventHandler
	public void onMissingMappings(FMLMissingMappingsEvent event) {
		for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
			EnumDyeColor color = null;
			if (mapping.name.equals(Ref.MOD_ID + ":BlockElevator")) {
				color = EnumDyeColor.WHITE;
			} else if (mapping.name.startsWith(Ref.MOD_ID + ":BlockElevator")) {
				String name = mapping.name.substring((Ref.MOD_ID + ":BlockElevator").length());
				for (EnumDyeColor candidate : EnumDyeColor.values()) {
					if (candidate.getUnlocalizedName().equalsIgnoreCase(name)) {
						color = candidate;
						break;
					}
				}
			}
			if (color != null) {
				switch (mapping.type) {
				case BLOCK:
					mapping.remap(ModBlocks.elevators.get(color));
					break;
				case ITEM:
					mapping.remap(ModItems.elevators.get(color));
					break;
				}
			}
		}
	}
}
