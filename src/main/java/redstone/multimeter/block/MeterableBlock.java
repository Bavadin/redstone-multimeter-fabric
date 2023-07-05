package redstone.multimeter.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import redstone.multimeter.interfaces.mixin.IServerWorld;

public interface MeterableBlock extends Meterable {

	default void rsmm$logPowered(World world, BlockPos pos, boolean powered) {
		if (!world.isClient()) {
			((IServerWorld)world).getMultimeter().logPowered(world, pos, powered);
		}
	}

	default void rsmm$logPowered(World world, BlockPos pos, BlockState state) {
		if (!world.isClient()) {
			((IServerWorld)world).getMultimeter().logPowered(world, pos, state);
		}
	}
}
