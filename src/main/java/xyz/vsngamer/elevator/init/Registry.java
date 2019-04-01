package xyz.vsngamer.elevator.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.render.ModelCamoElevator;
import xyz.vsngamer.elevator.tile.TileElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID)
public class Registry {

    public static EnumMap<EnumDyeColor, BlockElevator> elevatorsBlocks = new EnumMap<>(
            EnumDyeColor.class);
    public static EnumMap<EnumDyeColor, ItemBlock> elevatorsItems = new EnumMap<>(
            EnumDyeColor.class);

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator();
            block.setRegistryName(Ref.MOD_ID, "elevator_" + color.getName());
            block.setUnlocalizedName("elevator_" + color.getName());
            e.getRegistry().register(block);
            elevatorsBlocks.put(color, block);
        }

        GameRegistry.registerTileEntity(TileElevator.class, new ResourceLocation(Ref.MOD_ID, "tile_elevator"));
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            ItemBlock itemBlock = new ItemBlock(Registry.elevatorsBlocks.get(color));
            itemBlock.setRegistryName("elevator_" + color.getName());
            e.getRegistry().register(itemBlock);
            elevatorsItems.put(color, itemBlock);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        for (ItemBlock itemBlock : elevatorsItems.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                    new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        for (ItemBlock itemBlock : elevatorsItems.values()) {
            ModelResourceLocation tag = new ModelResourceLocation(itemBlock.getRegistryName().toString(), "normal");
            IBakedModel model = event.getModelRegistry().getObject(tag);

            event.getModelRegistry().putObject(tag, new ModelCamoElevator(model));
        }
    }

    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent e) {
        Registry.registerRenders();
    }
}
