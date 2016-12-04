package xyz.vsngamer.elevator.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.init.ModConfig;

public class ConfigGui extends GuiConfig{

	public ConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), Ref.MOD_ID, false, false, Ref.NAME);
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parentScreen){
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		
		list.add(new ConfigElement(ModConfig.config.getCategory("General")));
		
		return list;
	}

}
