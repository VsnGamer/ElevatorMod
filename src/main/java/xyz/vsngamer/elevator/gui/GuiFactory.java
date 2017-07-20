package xyz.vsngamer.elevator.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFactory extends DefaultGuiFactory {

    protected GuiFactory(String modid, String title) {
        super(modid, title);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ConfigGui(parentScreen);
    }

}
