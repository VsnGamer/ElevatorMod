package xyz.vsngamer.elevatorid.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
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

public class ElevatorScreen extends ContainerScreen<ElevatorContainer> {

    private final ResourceLocation GUI_TEXTURE = new ResourceLocation(ElevatorMod.ID, "textures/gui/elevator_gui.png");
    private final ElevatorTileEntity tile;
    private final Direction playerFacing;

    private FunctionalCheckbox dirButton;
    private FunctionalCheckbox hideArrowButton;
    private ExtendedButton resetCamoButton;
    private FacingControllerWrapper facingController;

    public ElevatorScreen(ElevatorContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        xSize = 200;
        ySize = 100;

        tile = container.getTile();
        playerFacing = container.getPlayerFacing();
    }

    @Override
    public void init() {
        super.init();

        // Toggle directional button
        ITextComponent dirLang = new TranslationTextComponent("screen.elevatorid.elevator.directional");
        dirButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 25, 20, 20, dirLang, tile.getBlockState().get(DIRECTIONAL), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetDirectionalPacket(value, tile.getPos())));
        addButton(dirButton);

        // Toggle arrow button
        ITextComponent arrowLang = new TranslationTextComponent("screen.elevatorid.elevator.hide_arrow");
        hideArrowButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 50, 20, 20, arrowLang, !tile.getBlockState().get(SHOW_ARROW), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetArrowPacket(!value, tile.getPos())));
        hideArrowButton.visible = tile.getBlockState().get(DIRECTIONAL);
        addButton(hideArrowButton);

        // Reset camouflage button
        ITextComponent resetCamoLang = new TranslationTextComponent("screen.elevatorid.elevator.reset_camo");
        resetCamoButton = new ExtendedButton(guiLeft + 8, guiTop + 75, 110, 20, resetCamoLang, p_onPress_1_ ->
                NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(tile.getPos())));
        addButton(resetCamoButton);

        // Directional controller
        facingController = new FacingControllerWrapper(guiLeft + 120, guiTop + 20, tile.getPos(), playerFacing);
        facingController.getButtons().forEach(this::addButton);
        facingController.getButtons().forEach(button -> button.visible = tile.getBlockState().get(DIRECTIONAL));

        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();

        facingController.getButtons().forEach(button -> {
            button.visible = dirButton.isChecked();
            button.active = tile.getBlockState().get(ElevatorBlock.HORIZONTAL_FACING) != button.direction;
        });

        hideArrowButton.visible = dirButton.isChecked();
        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float v, int mouseX, int mouseY) {
        if (minecraft != null) {
            minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        }

        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        font.drawText(matrixStack, title, 8.0F, 8.0F, 14737632);
    }
}
