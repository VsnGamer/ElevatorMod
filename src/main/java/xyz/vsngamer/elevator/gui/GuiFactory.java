package xyz.vsngamer.elevator.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory{

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends ConfigGui> mainConfigGuiClass() {
		return ConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}
	
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element){
		return null;
	}

}
