package xyz.vsngamer.elevator.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), Ref.MOD_ID, false, false, Ref.NAME);
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
        List<IConfigElement> list = new ArrayList<>();

        list.add(new ConfigElement(ElevatorMod.CONFIG.getCategory(Configuration.CATEGORY_GENERAL)));

        return list;
    }

}
