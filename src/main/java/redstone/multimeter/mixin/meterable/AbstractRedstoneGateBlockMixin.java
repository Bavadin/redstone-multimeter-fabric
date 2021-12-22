package redstone.multimeter.mixin.meterable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneComponentBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import redstone.multimeter.block.MeterableBlock;

@Mixin(RedstoneComponentBlock.class)
public abstract class AbstractRedstoneGateBlockMixin implements MeterableBlock {
	
	@Shadow @Final private boolean field_5539;
	
	@Shadow protected abstract boolean method_8727(World world, BlockPos pos, BlockState state);
	
	@Inject(
			method = "method_8727",
			at = @At(
					value = "RETURN"
			)
	)
	private void onPowerCheck(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		logPowered(world, pos, cir.getReturnValue()); // repeaters
	}
	
	@Override
	public boolean logPoweredOnBlockUpdate() {
		return false;
	}
	
	@Override
	public boolean isPowered(World world, BlockPos pos, BlockState state) {
		return method_8727(world, pos, state);
	}
	
	@Override
	public boolean isActive(World world, BlockPos pos, BlockState state) {
		return field_5539;
	}
}
