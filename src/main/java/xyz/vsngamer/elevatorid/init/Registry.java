package xyz.vsngamer.elevatorid.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.client.render.ElevatorBakedModel;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.EnumMap;

@Mod.EventBusSubscriber(modid = ElevatorMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final EnumMap<DyeColor, ElevatorBlock> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);

    public static final TileEntityType<ElevatorTileEntity> ELEVATOR_TILE_ENTITY;
    public static final ContainerType<ElevatorContainer> ELEVATOR_CONTAINER;

    static {
        for (DyeColor color : DyeColor.values()) {
            ELEVATOR_BLOCKS.put(color, new ElevatorBlock(color));
        }

        Block[] validBlocks = ELEVATOR_BLOCKS.values().toArray(new Block[0]);
        ELEVATOR_TILE_ENTITY = ElevatorTileEntity.buildTileType(validBlocks);
        ELEVATOR_CONTAINER = ElevatorContainer.buildContainerType();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        ELEVATOR_BLOCKS.values().forEach(block -> e.getRegistry().register(block.asItem()));
    }

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(ELEVATOR_TILE_ENTITY);
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> e){
        e.getRegistry().register(ELEVATOR_CONTAINER);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e){
        ELEVATOR_BLOCKS.values().forEach(block -> {
            ResourceLocation key = block.getRegistryName();
            IBakedModel originalModel = e.getModelRegistry().get(key);

            // Replace the default model with our custom IBakedModel, storing the default.
            e.getModelRegistry().put(key, new ElevatorBakedModel(originalModel));
        });
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
