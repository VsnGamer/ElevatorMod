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
import net.minecraftforge.oredict.OreDictionary;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.render.ModelCamoElevator;
import xyz.vsngamer.elevator.tile.TileElevator;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID)
public class Registry {

    public static EnumMap<EnumDyeColor, BlockElevator> ELEVATOR_BLOCKS = new EnumMap<>(EnumDyeColor.class);
    public static EnumMap<EnumDyeColor, ItemBlock> ELEVATOR_ITEMBLOCKS = new EnumMap<>(EnumDyeColor.class);

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator(color);
            ELEVATOR_BLOCKS.put(color, block);
            ELEVATOR_ITEMBLOCKS.put(color, block.new ItemBlockElevator());
        }
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> e) {
        ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block));
        GameRegistry.registerTileEntity(TileElevator.class, new ResourceLocation(Ref.MOD_ID, "tile_elevator"));
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> e) {
        ELEVATOR_ITEMBLOCKS.values().forEach(item -> {
            e.getRegistry().register(item);
            OreDictionary.registerOre("blockElevator", item);
        });
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        for (ItemBlock itemBlock : ELEVATOR_ITEMBLOCKS.values()) {
            ResourceLocation regName = itemBlock.getRegistryName();
            if (regName == null) {
                continue;
            }
            ModelResourceLocation tag = new ModelResourceLocation(regName.toString(), "normal");
            IBakedModel model = event.getModelRegistry().getObject(tag);

            event.getModelRegistry().putObject(tag, new ModelCamoElevator(model));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {
        ResourceLocation regName;
        for (ItemBlock itemBlock : ELEVATOR_ITEMBLOCKS.values()) {
            regName = itemBlock.getRegistryName();
            if (regName == null) {
                continue;
            }
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0,
                    new ModelResourceLocation(regName, "inventory"));
        }
    }
}
