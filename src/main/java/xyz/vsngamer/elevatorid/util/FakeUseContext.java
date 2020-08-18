package xyz.vsngamer.elevatorid.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;

import javax.annotation.Nonnull;

public class FakeUseContext extends BlockItemUseContext {
    public FakeUseContext(PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        super(new ItemUseContext(player, handIn, hit));
    }

    @Nonnull
    @Override
    public BlockPos getPos() {
        return func_242401_i().getPos();
    }
}
