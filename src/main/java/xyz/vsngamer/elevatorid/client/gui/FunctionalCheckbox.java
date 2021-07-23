package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

class FunctionalCheckbox extends Checkbox {

    private final Toggleable onPress;

    FunctionalCheckbox(int xIn, int yIn, int widthIn, int heightIn, Component text, boolean defaultValue, Toggleable onPress) {
        super(xIn, yIn, widthIn, heightIn, text, defaultValue);

        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(selected());
    }

    @FunctionalInterface
    public interface Toggleable {
        void onPress(boolean value);
    }
}
