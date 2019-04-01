package xyz.vsngamer.elevator.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class Properties {
    public static final IUnlistedProperty<IBlockState> HELD_STATE = new IUnlistedProperty<IBlockState>() {
        //ExtendedBlockState state;

        @Override
        public String getName() {
            return "HELD_STATE";
        }

        @Override
        public boolean isValid(IBlockState value) {
            return true;
        }

        @Override
        public Class<IBlockState> getType() {
            return IBlockState.class;
        }

        @Override
        public String valueToString(IBlockState value) {
            return value.toString();
        }
    };
}
