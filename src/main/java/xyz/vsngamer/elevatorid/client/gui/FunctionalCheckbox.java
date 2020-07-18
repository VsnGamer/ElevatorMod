package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;

class FunctionalCheckbox extends CheckboxButton {

    private final Toggleable onPress;

    FunctionalCheckbox(int xIn, int yIn, int widthIn, int heightIn, ITextComponent text, boolean defaultValue, Toggleable onPress) {
        super(xIn, yIn, widthIn, heightIn, text, defaultValue);

        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(isChecked());
    }

    @FunctionalInterface
    public interface Toggleable {
        void onPress(boolean value);
    }
}
