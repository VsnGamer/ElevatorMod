package com.vsngarcia.fabric.client;

import com.vsngarcia.ElevatorHandler;
import com.vsngarcia.client.gui.ElevatorScreen;
import com.vsngarcia.level.ElevatorContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.vsngarcia.fabric.FabricRegistry.ELEVATOR_CONTAINER;

@Environment(EnvType.CLIENT)
public final class ElevatorModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> ElevatorHandler.handleInput(ClientPlayNetworking::send));

//        ColorProviderRegistry.BLOCK.register(new ColorCamoElevator(), ELEVATOR_BLOCKS.values().toArray(new Block[0]));
//        ModelLoadingPlugin.register(new ElevatorModelLoadingPlugin());

        MenuScreens.register(
                ELEVATOR_CONTAINER,
                (ElevatorContainer container, Inventory inv, Component title) -> new ElevatorScreen(
                        container,
                        inv,
                        title,
                        ClientPlayNetworking::send
                )
        );
    }

//    public static class ElevatorModelLoadingPlugin implements ModelLoadingPlugin {
//
//        public static final ExtraModelKey<BlockStateModel> ARROW_MODEL_KEY = ExtraModelKey.create(() -> "arrow");
//        private static final Identifier ARROW_MODEL_ID = Identifier.fromNamespaceAndPath(ElevatorMod.ID, "arrow");
//
//        @Override
//        public void initialize(Context ctx) {
//            ctx.modifyBlockModelAfterBake().register(
//                    ModelModifier.WRAP_PHASE, (model, context) -> {
//                        if (!(context.state().getBlock() instanceof ElevatorBlock)) {
//                            return model;
//                        }
//
//                        ElevatorMod.LOGGER.debug("Wrapping elevator model: {}", context.state());
//                        return new ElevatorBakedModel(model);
//                    }
//            );
//
//            ctx.addModel(ARROW_MODEL_KEY, SimpleUnbakedExtraModel.blockStateModel(ARROW_MODEL_ID));
//        }
//    }
}
