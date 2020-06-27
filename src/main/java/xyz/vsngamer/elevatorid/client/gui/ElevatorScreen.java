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
    public void func_231160_c_() {
        super.func_231160_c_();

        // Toggle directional button
        ITextComponent dirLang = new TranslationTextComponent("screen.elevatorid.elevator.directional");
        dirButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 25, 20, 20, dirLang, tile.getBlockState().get(DIRECTIONAL), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetDirectionalPacket(value, tile.getPos())));
        func_230480_a_(dirButton);

        // Toggle arrow button
        ITextComponent arrowLang = new TranslationTextComponent("screen.elevatorid.elevator.hide_arrow");
        hideArrowButton = new FunctionalCheckbox(guiLeft + 8, guiTop + 50, 20, 20, arrowLang, !tile.getBlockState().get(SHOW_ARROW), value ->
                NetworkHandler.INSTANCE.sendToServer(new SetArrowPacket(!value, tile.getPos())));
        hideArrowButton.field_230694_p_ = tile.getBlockState().get(DIRECTIONAL);
        func_230480_a_(hideArrowButton);

        // Reset camouflage button
        ITextComponent resetCamoLang = new TranslationTextComponent("screen.elevatorid.elevator.reset_camo");
        resetCamoButton = new ExtendedButton(guiLeft + 8, guiTop + 75, 110, 20, resetCamoLang, p_onPress_1_ ->
                NetworkHandler.INSTANCE.sendToServer(new RemoveCamoPacket(tile.getPos())));
        func_230480_a_(resetCamoButton);

        // Directional controller
        facingController = new FacingControllerWrapper(guiLeft + 120, guiTop + 20, tile.getPos(), playerFacing);
        facingController.getButtons().forEach(this::func_230480_a_);
        facingController.getButtons().forEach(button -> button.field_230694_p_ = tile.getBlockState().get(DIRECTIONAL));

        resetCamoButton.field_230693_o_ = tile.getHeldState() != null;
    }

    @Override
    public void func_230430_a_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();

        facingController.getButtons().forEach(button -> {
            button.field_230694_p_ = dirButton.isChecked();
            button.field_230693_o_ = tile.getBlockState().get(ElevatorBlock.HORIZONTAL_FACING) != button.direction;
        });

        hideArrowButton.field_230694_p_ = dirButton.isChecked();
        resetCamoButton.field_230693_o_ = tile.getHeldState() != null;
    }

    @Override
    protected void func_230450_a_(@Nonnull MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        if (field_230706_i_ != null) {
            field_230706_i_.getTextureManager().bindTexture(GUI_TEXTURE);
        }

        int relX = (this.field_230708_k_ - this.xSize) / 2;
        int relY = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void func_230451_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        field_230712_o_.func_238407_a_(matrixStack, field_230704_d_, 8.0F, 8.0F, 14737632);
    }
}
