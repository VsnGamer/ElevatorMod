package xyz.vsngamer.elevator.blocks;

import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.vsngamer.elevator.ElevatorMod;
import xyz.vsngamer.elevator.Ref;

import javax.annotation.Nonnull;

/**
 * @author Mitchell Skaggs
 */
public class BlockElevator extends BlockColored implements IInitializer, IModelRegister {
    public static final String NAME = "elevator";

    protected String modName;
    protected String name;

    public BlockElevator() {
        super(Material.CLOTH);
        this.modName = Ref.MOD_ID;

        setUnlocalizedName(NAME);
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        setHardness(0.8F);
        setSoundType(SoundType.CLOTH);
    }

    @Override
    @Nonnull
    public Block setUnlocalizedName(@Nonnull String name) {
        this.name = name;
        name = modName + "." + name;
        return super.setUnlocalizedName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    dyeColor.getMetadata(),
                    new ModelResourceLocation(modName + ":" + name, "color=" + dyeColor.getName()));
        }
    }

    @Override
    public boolean initialize() {
        this.setRegistryName("elevator");
        ForgeRegistries.BLOCKS.register(this);

        //noinspection ConstantConditions
        ItemBlockElevator itemBlockElevator = new ItemBlockElevator(this);
        ForgeRegistries.ITEMS.register(itemBlockElevator);

        ElevatorMod.proxy.addIModelRegister(this);

        return true;
    }

    @Override
    public boolean register() {
        for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
            RecipeHelper.addShapedRecipe(
                    new ItemStack(this, 1, dyeColor.getMetadata()),
                    "WWW",
                    "WEW",
                    "WWW",
                    'E', Items.ENDER_PEARL,
                    'W', new ItemStack(Blocks.WOOL, 1, dyeColor.getMetadata()));
        }
        return true;
    }
}
