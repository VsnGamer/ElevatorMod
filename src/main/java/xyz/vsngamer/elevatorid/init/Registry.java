package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.Ref;
import xyz.vsngamer.elevatorid.blocks.BlockElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    private static EnumMap<EnumDyeColor, BlockElevator> elevatorsBlocks = new EnumMap<>(
            EnumDyeColor.class);
    public static EnumMap<EnumDyeColor, ItemBlock> elevatorsItems = new EnumMap<>(
            EnumDyeColor.class);

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator();
            block.setRegistryName(Ref.MOD_ID, "elevator_" + color.getName());
            //block.setUnlocalizedName("elevator_" + color.getName()); //Not needed anymore
            e.getRegistry().register(block);
            elevatorsBlocks.put(color, block);
        }
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block elevatorBlock = elevatorsBlocks.get(color);
            ItemBlock itemBlock = new ItemBlock(elevatorBlock, new Item.Properties().group(ElevatorMod.CREATIVE_TAB));
            itemBlock.setRegistryName("elevator_" + color.getName());
            e.getRegistry().register(itemBlock);
            elevatorsItems.put(color, itemBlock);
        }
    }

    //this is probably going to be removed (https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a#rendering-changes)
    //@SideOnly(Side.CLIENT)
//    public static void registerRenders() {
//        for (ItemBlock itemBlock : elevatorsItems.values()) {
//            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
//                    new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
//        }
//    }

//    @SubscribeEvent
//    public static void registerModels(final ModelRegistryEvent e) {
//        Registry.registerRenders();
//    }




//    @SubscribeEvent //TODO: Config
//    public static void configChanged(ConfigChangedEvent event) {
//        if (event.getModID().equals(Ref.MOD_ID)) {
//            ConfigManager.sync(Ref.MOD_ID, Config.Type.INSTANCE);
//        }
//    }
}
