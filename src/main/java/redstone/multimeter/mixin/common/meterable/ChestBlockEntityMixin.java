package redstone.multimeter.mixin.common.meterable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;

import redstone.multimeter.interfaces.mixin.IChestBlockEntity;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements IChestBlockEntity {
	
	@Inject(
			method = "onOpen",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/block/entity/ChestBlockEntity;onInvOpenOrClose()V"
			)
	)
	private void onOpenedByPlayer(PlayerEntity player, CallbackInfo ci) {
		invOpenOrCloseRSMM(true);
	}
	
	@Inject(
			method = "onClose",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/block/entity/ChestBlockEntity;onInvOpenOrClose()V"
			)
	)
	private void onClosedByPlayer(PlayerEntity player, CallbackInfo ci) {
		invOpenOrCloseRSMM(false);
	}
}