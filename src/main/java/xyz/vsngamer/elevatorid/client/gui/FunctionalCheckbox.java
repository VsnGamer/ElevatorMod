package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.widget.button.CheckboxButton;

class FunctionalCheckbox extends CheckboxButton {

    private final Toggleable onPress;

    FunctionalCheckbox(int xIn, int yIn, int widthIn, int heightIn, String msg, boolean defaultValue, Toggleable onPress) {
        super(xIn, yIn, widthIn, heightIn, msg, defaultValue);

        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(func_212942_a());
    }

    @FunctionalInterface
    public interface Toggleable {
        void onPress(boolean value);
    }
}
