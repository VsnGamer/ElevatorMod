package xyz.vsngamer.elevatorid.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import java.util.Arrays;
import java.util.EnumMap;

public class Registry {
    public static final EnumMap<DyeColor, RegistryObject<ElevatorBlock>> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElevatorMod.ID);

    static {
        Arrays.stream(DyeColor.values()).forEach(color ->
                ELEVATOR_BLOCKS.put(color, BLOCKS.register("elevator_" + color.getName(), () -> new ElevatorBlock(color)))
        );
    }

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ElevatorMod.ID);
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

    public static final EnumMap<DyeColor, RegistryObject<BlockItem>> ELEVATOR_ITEMS = new EnumMap<>(DyeColor.class);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElevatorMod.ID);

    static {
        ELEVATOR_BLOCKS.forEach((color, o) ->
                ELEVATOR_ITEMS.put(color, ITEMS.register("elevator_" + color.getName(), () -> new BlockItem(o.get(), new Item.Properties())))
        );
    }

    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ElevatorMod.ID);
    public static final RegistryObject<MenuType<ElevatorContainer>> ELEVATOR_CONTAINER = CONTAINERS.register(
            "elevator_container", () ->
                    IMenuTypeExtension.create((windowId, inv, data) ->
                            new ElevatorContainer(windowId, data.readBlockPos(), inv.player)
                    )
    );

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ElevatorMod.ID);
    public static final RegistryObject<SoundEvent> TELEPORT_SOUND = SOUNDS.register(
            "teleport", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ElevatorMod.ID, "teleport"))
    );
    public static final RegistryObject<SoundEvent> CAMOUFLAGE_SOUND = SOUNDS.register(
            "camouflage", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ElevatorMod.ID, "camouflage"))
    );

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ElevatorMod.ID);

    private static final RegistryObject<CreativeModeTab> ELEVATORS_TAB = CREATIVE_TABS.register("elevators_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> ELEVATOR_ITEMS.get(DyeColor.WHITE).get().getDefaultInstance())
                    .displayItems((params, output) -> ELEVATOR_ITEMS.values().forEach(item -> output.accept(item.get())))
                    .title(Component.translatable("itemGroup.elevators_tab"))
                    .build()
    );

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
        SOUNDS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }

    // TODO: Config GUI
    /*@SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ElevatorMod.ID)) {
            ConfigManager.sync(ElevatorMod.ID, Config.Type.INSTANCE);
        }
    }*/
}
