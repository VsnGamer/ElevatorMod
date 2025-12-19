package com.vsngarcia.client.gui;

import com.vsngarcia.ElevatorBlockBase;
import com.vsngarcia.ElevatorMod;
import com.vsngarcia.level.ElevatorBlockEntityBase;
import com.vsngarcia.level.ElevatorContainer;
import com.vsngarcia.network.ClientPacketSender;
import com.vsngarcia.network.client.RemoveCamoPacket;
import com.vsngarcia.network.client.SetArrowPacket;
import com.vsngarcia.network.client.SetDirectionalPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;


public class ElevatorScreen extends AbstractContainerScreen<ElevatorContainer> {

    private final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(
            ElevatorMod.ID,
            "textures/gui/elevator_gui.png"
    );
    private final ElevatorBlockEntityBase tile;
    private final Direction playerFacing;
    private final ClientPacketSender packetSender;

    private Checkbox dirButton;
    private Checkbox hideArrowButton;
    private Button resetCamoButton;
    private FacingControllerWrapper facingController;

    public ElevatorScreen(ElevatorContainer container, Inventory inv, Component titleIn, ClientPacketSender pktSender) {
        super(container, inv, titleIn);
        imageWidth = 200;
        imageHeight = 100;

        tile = container.getTile();
        playerFacing = container.getPlayerFacing();

        packetSender = pktSender;
    }

    @Override
    public void init() {
        super.init();

        // Toggle directional button
        dirButton = Checkbox.builder(Component.translatable("screen.elevatorid.elevator.directional"), font)
                .pos(leftPos + 8, topPos + 25)
                .selected(tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL))
                .onValueChange((checkbox, selected) -> packetSender.sendToServer(new SetDirectionalPacket(
                        selected,
                        tile.getBlockPos()
                )))
                .build();
        addRenderableWidget(dirButton);

        // Toggle arrow button
        hideArrowButton = Checkbox.builder(Component.translatable("screen.elevatorid.elevator.hide_arrow"), font)
                .pos(leftPos + 8, topPos + 50)
                .selected(!tile.getBlockState().getValue(ElevatorBlockBase.SHOW_ARROW))
                .onValueChange((checkbox, selected) -> packetSender.sendToServer(new SetArrowPacket(
                        !selected,
                        tile.getBlockPos()
                )))
                .build();
        hideArrowButton.visible = tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL);
        addRenderableWidget(hideArrowButton);

        // Directional controller
        facingController = new FacingControllerWrapper(
                leftPos + 120,
                topPos + 20,
                tile.getBlockPos(),
                playerFacing,
                packetSender
        );
        facingController.getButtons().forEach(this::addRenderableWidget);
        facingController.getButtons().forEach(button -> {
            button.visible = tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL);
            button.active = tile.getBlockState().getValue(ElevatorBlockBase.FACING) != button.direction;
        });

        // Reset camouflage button
        resetCamoButton = Button.builder(
                        Component.translatable("screen.elevatorid.elevator.reset_camo"),
                        but -> packetSender.sendToServer(new RemoveCamoPacket(tile.getBlockPos()))
                )
                .pos(leftPos + 8, topPos + 75)
                .size(110, 20)
                .build();
        addRenderableWidget(resetCamoButton);
        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void containerTick() {
        super.containerTick();

        dirButton.selected = tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL);

        facingController.getButtons().forEach(button -> {
            button.visible = tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL);
            button.active = tile.getBlockState().getValue(ElevatorBlockBase.FACING) != button.direction;
        });

        hideArrowButton.visible = tile.getBlockState().getValue(ElevatorBlockBase.DIRECTIONAL);
        hideArrowButton.selected = !tile.getBlockState().getValue(ElevatorBlockBase.SHOW_ARROW);

        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                GUI_TEXTURE,
                (this.width - this.imageWidth) / 2,
                (this.height - this.imageHeight) / 2,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, 8, 8, 0xFFE0E0E0);
    }
}
