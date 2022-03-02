package xyz.vsngamer.elevatorid.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.ElevatorModTab;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.Arrays;
import java.util.EnumMap;

public class Registry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElevatorMod.ID);
    public static final EnumMap<DyeColor, RegistryObject<ElevatorBlock>> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);

    static {
        Arrays.stream(DyeColor.values()).forEach(color ->
                ELEVATOR_BLOCKS.put(color, BLOCKS.register("elevator_" + color.getName(), () -> new ElevatorBlock(color)))
        );
    }

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ElevatorMod.ID);
    public static final RegistryObject<BlockEntityType<ElevatorTileEntity>> ELEVATOR_TILE_ENTITY = BLOCK_ENTITIES.register(
            "elevator_tile", () ->
                    BlockEntityType.Builder.of(
                            ElevatorTileEntity::new,
                            ELEVATOR_BLOCKS
                                    .values()
                                    .stream()
                                    .map(RegistryObject::get)
                                    .toArray(Block[]::new)
                    ).build(null)
    );

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElevatorMod.ID);

    static {
        ELEVATOR_BLOCKS.forEach((color, o) ->
                ITEMS.register("elevator_" + color.getName(), () -> new BlockItem(o.get(), new Item.Properties().tab(ElevatorModTab.TAB)))
        );
    }

    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ElevatorMod.ID);
    public static final RegistryObject<MenuType<ElevatorContainer>> ELEVATOR_CONTAINER = CONTAINERS.register(
            "elevator_container", () ->
                    IForgeMenuType.create((windowId, inv, data) ->
                            new ElevatorContainer(windowId, data.readBlockPos(), inv.player)
                    )
    );

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
