package xyz.vsngamer.elevatorid.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.blocks.ElevatorBlock;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.RemoveCamoPacket;
import xyz.vsngamer.elevatorid.network.client.SetArrowPacket;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import static xyz.vsngamer.elevatorid.blocks.ElevatorBlock.DIRECTIONAL;
import static xyz.vsngamer.elevatorid.blocks.ElevatorBlock.SHOW_ARROW;

public class ElevatorScreen extends ContainerScreen<ElevatorContainer> {

    private final ResourceLocation GUI_TEXTURE = new ResourceLocation(ElevatorMod.ID, "textures/gui/elevator_gui.png");
    private ElevatorTileEntity tile;
    private Direction playerFacing;

    private FunctionalCheckbox dirButton;
    private FunctionalCheckbox hideArrowButton;
    private GuiButtonExt resetCamoButton;
    private FacingControllerWrapper facingController;

    public ElevatorScreen(ElevatorContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        xSize = 200;
        ySize = 100;

        tile = container.getTile();
        playerFacing = container.getPlayerFacing();
    }

    @Override
    protected void init() {
        super.init();

        // Toggle directional button
        String dirLang = new TranslationTextComponent("screen.elevatorid.elevator.directional").getFormattedText();
        dirButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 25, 20, 20, dirLang, tile.getBlockState().get(DIRECTIONAL), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetDirectionalPacket(value, tile.getPos())));
        addButton(dirButton);

        // Toggle arrow button
        String arrowLang = new TranslationTextComponent("screen.elevatorid.elevator.hide_arrow").getFormattedText();
        hideArrowButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 50, 20, 20, arrowLang, !tile.getBlockState().get(SHOW_ARROW), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetArrowPacket(!value, tile.getPos())));
        hideArrowButton.visible = false;
        addButton(hideArrowButton);

        // Reset camouflage button
        String resetCamoLang = new TranslationTextComponent("screen.elevatorid.elevator.reset_camo").getFormattedText();
        resetCamoButton = new GuiButtonExt(guiLeft + 8, guiTop + 75, 110, 20, resetCamoLang, p_onPress_1_ ->
                NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(tile.getPos())));
        addButton(resetCamoButton);

        // Directional controller
        facingController = new FacingControllerWrapper(guiLeft + 120, guiTop + 20, tile.getPos(), playerFacing);
        facingController.getButtons().forEach(this::addButton);
        facingController.getButtons().forEach(button -> button.visible = false);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);


    }

    @Override
    public void tick() {
        super.tick();

        facingController.getButtons().forEach(button -> {
            button.visible = dirButton.func_212942_a();
            button.active = tile.getBlockState().get(ElevatorBlock.HORIZONTAL_FACING) != button.direction;
        });

        hideArrowButton.visible = dirButton.func_212942_a();
        resetCamoButton.active = tile.getHeldState() != null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (minecraft != null) {
            minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        }
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 8.0F, 14737632);
    }
}
