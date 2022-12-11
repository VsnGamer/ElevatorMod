package xyz.vsngamer.elevatorid;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.init.Registry;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElevatorModTab {

    public static CreativeModeTab TAB;

//    private ElevatorModTab() {
//        super("elevators_tab");
//    }

//    @Override
//    @Nonnull
//    public ItemStack makeIcon() {
//        return new ItemStack(Registry.ELEVATOR_BLOCKS.get(DyeColor.WHITE).get());
//    }

    @SubscribeEvent
    public static void register(CreativeModeTabEvent.Register event) {
        TAB = event.registerCreativeModeTab(
                new ResourceLocation("elevators_tab", ElevatorMod.ID), builder -> builder
                        .icon(() -> new ItemStack(Registry.ELEVATOR_BLOCKS.get(DyeColor.WHITE).get()))
                        .displayItems((featureFlags, output, hasOp) -> Registry.ELEVATOR_ITEMS.values().forEach(o -> output.accept(o.get())))
                        .title(Component.translatable("itemGroup.elevators_tab"))
        );
    }
}
