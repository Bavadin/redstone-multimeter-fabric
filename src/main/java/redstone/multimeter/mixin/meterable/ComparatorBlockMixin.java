package redstone.multimeter.mixin.meterable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.world.World;

import redstone.multimeter.block.MeterableBlock;
import redstone.multimeter.block.PowerSource;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin implements MeterableBlock, PowerSource {
	
	@Inject(
			method = "method_4757",
			at = @At(
					value = "RETURN"
			)
	)
	private void onPowerCheck(World world, int x, int y, int z, int metadata, CallbackInfoReturnable<Integer> cir) {
		logPowered(world, x, y, z, cir.getReturnValue() > MIN_POWER);
	}
	
	@Override
	public boolean isActive(World world, int x, int y, int z, int metadata) {
		return getPowerLevel(world, x, y, z, metadata) > MIN_POWER;
	}
	
	@Override
	public boolean logPowerChangeOnStateChange() {
		return false;
	}
	
	@Override
	public int getPowerLevel(World world, int x, int y, int z, int metadata) {
		BlockEntity blockEntity = world.method_3781(x, y, z);
		
		if (blockEntity instanceof ComparatorBlockEntity) {
			return ((ComparatorBlockEntity)blockEntity).getOutputSignal();
		}
		
		return MIN_POWER;
	}
}
