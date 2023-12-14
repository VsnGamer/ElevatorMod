package xyz.vsngamer.elevatorid.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import javax.annotation.Nonnull;

import static xyz.vsngamer.elevatorid.blocks.ElevatorBlock.DIRECTIONAL;
import static xyz.vsngamer.elevatorid.blocks.ElevatorBlock.SHOW_ARROW;

public class ElevatorScreen extends AbstractContainerScreen<ElevatorContainer> {

    private final ResourceLocation GUI_TEXTURE = new ResourceLocation(ElevatorMod.ID, "textures/gui/elevator_gui.png");
    private final ElevatorTileEntity tile;
    private final Direction playerFacing;

    private Checkbox dirButton;
    private Checkbox hideArrowButton;
    private Button resetCamoButton;
    private FacingControllerWrapper facingController;

    public ElevatorScreen(ElevatorContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        imageWidth = 200;
        imageHeight = 100;

        tile = container.getTile();
        playerFacing = container.getPlayerFacing();
    }

    @Override
    public void init() {
        super.init();

        // Toggle directional button
        Component dirLang = Component.translatable("screen.elevatorid.elevator.directional");
        dirButton = Checkbox.builder(dirLang, font).pos(leftPos + 8, topPos + 25).selected(tile.getBlockState().getValue(DIRECTIONAL)).onValueChange(
                (checkbox, selected) -> NetworkHandler.INSTANCE.sendToServer(new SetDirectionalPacket(selected, tile.getBlockPos()))
        ).build();
        addRenderableWidget(dirButton);

        // Toggle arrow button
        Component arrowLang = Component.translatable("screen.elevatorid.elevator.hide_arrow");
        hideArrowButton = Checkbox.builder(arrowLang, font).pos(leftPos + 8, topPos + 50).selected(!tile.getBlockState().getValue(SHOW_ARROW)).onValueChange(
                (checkbox, selected) -> NetworkHandler.INSTANCE.sendToServer(new SetArrowPacket(!selected, tile.getBlockPos()))
        ).build();
        hideArrowButton.visible = tile.getBlockState().getValue(DIRECTIONAL);
        addRenderableWidget(hideArrowButton);

        // Reset camouflage button
        Component resetCamoLang = Component.translatable("screen.elevatorid.elevator.reset_camo");
        resetCamoButton = Button.builder(resetCamoLang, but -> NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(tile.getBlockPos()))).pos(leftPos + 8, topPos + 75).size(110, 20).build();
        addRenderableWidget(resetCamoButton);

        // Directional controller
        facingController = new FacingControllerWrapper(leftPos + 120, topPos + 20, tile.getBlockPos(), playerFacing);
        facingController.getButtons().forEach(this::addRenderableWidget);
        facingController.getButtons().forEach(button -> {
            button.visible = tile.getBlockState().getValue(DIRECTIONAL);
            button.active = tile.getBlockState().getValue(ElevatorBlock.FACING) != button.direction;
        });

        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void containerTick() {
        super.containerTick();

        dirButton.selected = tile.getBlockState().getValue(ElevatorBlock.DIRECTIONAL);

        facingController.getButtons().forEach(button -> {
            button.visible = tile.getBlockState().getValue(DIRECTIONAL);
            button.active = tile.getBlockState().getValue(ElevatorBlock.FACING) != button.direction;
        });

        hideArrowButton.visible = tile.getBlockState().getValue(DIRECTIONAL);
        hideArrowButton.selected = !tile.getBlockState().getValue(SHOW_ARROW);

        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, 8, 8, 14737632);
    }
}
