package xyz.vsngamer.elevator.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevator.init.Registry;

@JEIPlugin
public class ElevatorJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        IIngredientType<ItemStack> type = registry.getIngredientRegistry().getIngredientType(ItemStack.class);

        Registry.elevatorsItems.values().forEach(item -> registry.addIngredientInfo(new ItemStack(item), type, "jei.description.elevators"));
    }
}
