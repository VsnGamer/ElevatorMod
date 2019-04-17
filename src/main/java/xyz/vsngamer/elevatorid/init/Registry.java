package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    private static EnumMap<EnumDyeColor, BlockElevator> ELEVATOR_BLOCKS = new EnumMap<>(EnumDyeColor.class);
    public static EnumMap<EnumDyeColor, ItemBlock> ELEVATOR_ITEMBLOCKS = new EnumMap<>(EnumDyeColor.class);


    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator(color);
            ELEVATOR_BLOCKS.put(color, block);
            ELEVATOR_ITEMBLOCKS.put(color, block.new ItemBlockElevator());
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        for (BlockElevator block : ELEVATOR_BLOCKS.values()) {
            e.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for (ItemBlock item : ELEVATOR_ITEMBLOCKS.values()) {
            e.getRegistry().register(item);
        }
    }

    // This is probably going to be removed (https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a#rendering-changes)
    /*@SideOnly(Side.CLIENT)
    public static void registerRenders() {
        for (ItemBlock itemBlock : elevatorsItems.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                    new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent e) {
        Registry.registerRenders();
    }*/


    // TODO: Config
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
