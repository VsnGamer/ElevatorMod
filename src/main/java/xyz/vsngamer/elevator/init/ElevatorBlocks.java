package xyz.vsngamer.elevator.init;

import cofh.core.util.core.IInitializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.vsngamer.elevator.blocks.BlockElevator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mitchell Skaggs
 */
public class ElevatorBlocks {
    public static final ElevatorBlocks INSTANCE = new ElevatorBlocks();

    public static BlockElevator elevator;

    private static List<IInitializer> initializerList = new ArrayList<>(1);

    private ElevatorBlocks() {
    }

    public static void preInit() {
        elevator = new BlockElevator();

        initializerList.add(elevator);

        for (IInitializer initializer : initializerList) {
            initializer.initialize();
        }

        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (IInitializer initializer : initializerList) {
            initializer.register();
        }
    }
}
