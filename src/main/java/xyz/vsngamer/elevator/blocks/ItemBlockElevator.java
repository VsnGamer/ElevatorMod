package xyz.vsngamer.elevator.blocks;

import cofh.core.block.ItemBlockCore;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import xyz.vsngamer.elevator.Ref;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * @author Mitchell Skaggs
 */
public class ItemBlockElevator extends ItemBlockCore {
    public ItemBlockElevator(BlockElevator block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
        setRegistryName(requireNonNull(block.getRegistryName(), "Block (" + block + ") registry name is null!"));
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return "tile."
                + Ref.MOD_ID + "."
                + BlockElevator.NAME + "."
                + EnumDyeColor.byMetadata(ItemHelper.getItemDamage(stack)).getName() + "."
                + "name";
    }
}
