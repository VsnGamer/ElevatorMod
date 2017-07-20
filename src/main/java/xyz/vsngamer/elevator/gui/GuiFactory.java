package xyz.vsngamer.elevator.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;

import java.util.Collections;

public class GuiFactory extends DefaultGuiFactory {

    public GuiFactory() {
        super(Ref.MOD_ID, Ref.NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiConfig(parentScreen,
                Collections.singletonList(
                        new ConfigElement(ElevatorMod.CONFIG.getCategory(Configuration.CATEGORY_GENERAL))
                ),
                modid,
                false,
                false,
                title);
    }

}
