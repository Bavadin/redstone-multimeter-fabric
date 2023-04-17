package redstone.multimeter.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;

import redstone.multimeter.command.MeterGroupCommand;

@Mixin(Commands.class)
public class CommandsMixin {

	@Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(
		method="<init>",
		at = @At(
			value = "INVOKE",
			shift = Shift.BEFORE,
			target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"
		)
	)
	private void registerCommands(CommandSelection selection, CommandBuildContext buildContext, CallbackInfo ci) {
		MeterGroupCommand.register(dispatcher, buildContext);
	}
}
