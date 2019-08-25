package xyz.vsngamer.elevatorid.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.vsngamer.elevatorid.ElevatorMod;
import xyz.vsngamer.elevatorid.network.NetworkHandler;
import xyz.vsngamer.elevatorid.network.client.SetDirectionalPacket;
import xyz.vsngamer.elevatorid.tile.ElevatorContainer;
import xyz.vsngamer.elevatorid.tile.ElevatorTileEntity;

import static xyz.vsngamer.elevatorid.blocks.ElevatorBlock.DIRECTIONAL;

public class ElevatorScreen extends ContainerScreen<ElevatorContainer> {

    private final ResourceLocation GUI_TEXTURE = new ResourceLocation(ElevatorMod.ID, "textures/gui/elevator_gui.png");
    private ElevatorTileEntity tile;

    private FunctionalCheckbox dirButton;
    private FunctionalCheckbox hideArrowButton;

    public ElevatorScreen(ElevatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        ySize = 64;
        tile = screenContainer.getTile();
    }

    @Override
    protected void init() {
        super.init();
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        //int relXF = (this.width + this.xSize) / 2;
        int relYF = (this.height + this.ySize) / 2;
        int middleY = (relY + relYF) / 2 - 10;

        String dirLang = new TranslationTextComponent("screen.elevatorid.elevator.directional").getFormattedText();
        dirButton = new FunctionalCheckbox(relX + 8, middleY + 5, 20, 20, dirLang, tile.getBlockState().get(DIRECTIONAL), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetDirectionalPacket(value, tile.getPos())));
        addButton(dirButton);

        String arrowLang = new TranslationTextComponent("screen.elevatorid.elevator.hide_arrow").getFormattedText();
        hideArrowButton = new FunctionalCheckbox(relX + 90, middleY + 5, 20, 20, arrowLang, false, value -> {
        });
        hideArrowButton.visible = false;
        addButton(hideArrowButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);

        hideArrowButton.visible = dirButton.func_212942_a();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
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
