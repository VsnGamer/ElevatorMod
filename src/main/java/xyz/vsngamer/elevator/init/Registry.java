package xyz.vsngamer.elevator.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import xyz.vsngamer.elevator.Ref;
import xyz.vsngamer.elevator.blocks.BlockElevator;
import xyz.vsngamer.elevator.network.NetworkHandler;
import xyz.vsngamer.elevator.network.SyncConfig;
import xyz.vsngamer.elevator.network.SyncConfigHandler;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID)
public class Registry {

    private static EnumMap<EnumDyeColor, BlockElevator> ELEVATOR_BLOCKS = new EnumMap<>(EnumDyeColor.class);
    public static EnumMap<EnumDyeColor, ItemBlock> ELEVATOR_ITEMBLOCKS = new EnumMap<>(EnumDyeColor.class);

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockElevator block = new BlockElevator(color);
            e.getRegistry().register(block);

            ELEVATOR_BLOCKS.put(color, block);
        }
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> e) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            ItemBlock itemBlock = new ItemBlock(Registry.ELEVATOR_BLOCKS.get(color));
            itemBlock.setRegistryName("elevator_" + color.getName());
            e.getRegistry().register(itemBlock);

            ELEVATOR_ITEMBLOCKS.put(color, itemBlock);

            OreDictionary.registerOre("blockElevator", itemBlock);
        }
    }

    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent e) {
        for (ItemBlock itemBlock : ELEVATOR_ITEMBLOCKS.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Ref.MOD_ID)) {
            ConfigManager.sync(Ref.MOD_ID, Config.Type.INSTANCE);

            // SINGLE PLAYER
            if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                SyncConfigHandler.setClientConfig(ModConfig.serverConfig.sameColor, ModConfig.serverConfig.range);
            }
        }
    }

    @SubscribeEvent
    public static void syncServerConfig(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        NetworkHandler.networkWrapper.sendTo(new SyncConfig(ModConfig.serverConfig.sameColor, ModConfig.serverConfig.range), player);
    }
}
