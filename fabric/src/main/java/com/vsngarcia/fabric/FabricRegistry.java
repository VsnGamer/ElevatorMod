package com.vsngarcia.fabric;

import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.fabric.tile.ElevatorBlockEntity;
import com.vsngarcia.level.ElevatorContainer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;

public class FabricRegistry {

    public static final EnumMap<DyeColor, Block> ELEVATOR_BLOCKS = new EnumMap<>(DyeColor.class);

    static {
        Arrays.stream(DyeColor.values()).forEach(color ->
                ELEVATOR_BLOCKS.put(
                        color,
                        Registry.register(
                                BuiltInRegistries.BLOCK,
                                ElevatorBlockBase.getResourceLocation(color),
                                new ElevatorBlock(color)
                        )
                )
        );
    }

    public static final EnumMap<DyeColor, BlockItem> ELEVATOR_ITEMS = new EnumMap<>(DyeColor.class);

    static {
        ELEVATOR_BLOCKS.forEach(
                (color, block) -> ELEVATOR_ITEMS.put(
                        color,
                        Registry.register(
                                BuiltInRegistries.ITEM,
                                ElevatorBlockBase.getResourceLocation(color),
                                new BlockItem(block, new BlockItem.Properties().useBlockDescriptionPrefix().setId(
                                        ElevatorBlockBase.getResourceKey(Registries.ITEM, color)
                                ))
                        )
                )
        );
    }

    public static final BlockEntityType<ElevatorBlockEntity> ELEVATOR_BLOCK_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(ElevatorMod.ID, "elevator_tile"),
            new BlockEntityType<>(ElevatorBlockEntity::new, new HashSet<>(ELEVATOR_BLOCKS.values()))
    );

    public static ExtendedScreenHandlerType<ElevatorContainer, ElevatorContainerData> ELEVATOR_CONTAINER = null;

    static {
        ELEVATOR_CONTAINER = new ExtendedScreenHandlerType<>(
                (syncId, inventory, data) -> new ElevatorContainer(
                        ELEVATOR_CONTAINER,
                        syncId,
                        data.pos(),
                        inventory.player
                ),
                ElevatorContainerData.CODEC
        );

        Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(ElevatorMod.ID, "elevator_container"),
                ELEVATOR_CONTAINER
        );
    }

    public static final SoundEvent TELEPORT_SOUND = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            Identifier.fromNamespaceAndPath(ElevatorMod.ID, "teleport"),
            SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ElevatorMod.ID, "teleport"))
    );

    public static final SoundEvent CAMOUFLAGE_SOUND = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            Identifier.fromNamespaceAndPath(ElevatorMod.ID, "camouflage"),
            SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ElevatorMod.ID, "camouflage"))
    );

    static {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(ElevatorMod.ID, "elevators_tab"),
                FabricItemGroup.builder()
                        .icon(() -> ELEVATOR_BLOCKS.get(DyeColor.WHITE).asItem().getDefaultInstance())
                        .displayItems((params, output) -> ELEVATOR_ITEMS.values().forEach(output::accept))
                        .title(Component.translatable("itemGroup.elevators_tab"))
                        .build()
        );
    }

    public static void init() {
        // Register our items and blocks here.
    }

    public record ElevatorContainerData(BlockPos pos) {
        public static final StreamCodec<RegistryFriendlyByteBuf, ElevatorContainerData> CODEC =
                StreamCodec.composite(BlockPos.STREAM_CODEC, ElevatorContainerData::pos, ElevatorContainerData::new);
    }
}

