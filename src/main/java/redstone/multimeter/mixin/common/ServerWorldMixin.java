package redstone.multimeter.mixin.common;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;

import redstone.multimeter.common.TickTask;
import redstone.multimeter.interfaces.mixin.IMinecraftServer;
import redstone.multimeter.interfaces.mixin.IServerWorld;
import redstone.multimeter.server.MultimeterServer;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements IServerWorld {
	
	@Shadow @Final private MinecraftServer server;
	@Shadow @Final private ObjectLinkedOpenHashSet<BlockEvent> syncedBlockEventQueue;
	@Shadow @Final private boolean field_25143;
	
	private int currentDepth;
	private int currentBatch;
	
	protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
		super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "HEAD"
			)
	)
	private void startTickTaskTickWorld(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		startTickTaskRSMM(TickTask.TICK_WORLD, getRegistryKey().getValue().toString());
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
					args = "ldc=world border"
			)
	)
	private void startTickTaskWorldBorder(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		startTickTaskRSMM(TickTask.WORLD_BORDER);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=weather"
			)
	)
	private void swapTickTaskWeather(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.WEATHER);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=chunkSource"
			)
	)
	private void swapTickTaskChunkSource(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.CHUNK_SOURCE);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=tickPending"
			)
	)
	private void swapTickTaskScheduledTicks(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.SCHEDULED_TICKS);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
			)
	)
	private void startTickTaskBlockTicks(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		startTickTaskRSMM(TickTask.BLOCK_TICKS);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					ordinal = 0,
					shift = Shift.AFTER,
					target = "Lnet/minecraft/server/world/ServerTickScheduler;tick()V"
			)
	)
	private void endTickTaskBlockTicks(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		endTickTaskRSMM();
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
			)
	)
	private void startTickTaskFluidTicks(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		startTickTaskRSMM(TickTask.FLUID_TICKS);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					ordinal = 1,
					shift = Shift.AFTER,
					target = "Lnet/minecraft/server/world/ServerTickScheduler;tick()V"
			)
	)
	private void endTickTaskFluidTicks(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		endTickTaskRSMM();
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=raid"
			)
	)
	private void swapTickTaskRaids(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.RAIDS);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=blockEvents"
			)
	)
	private void swapTickTaskBlockEvents(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.BLOCK_EVENTS);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=entities"
			)
	)
	private void swapTickTaskEntities(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		swapTickTaskRSMM(TickTask.ENTITIES);
	}
	
	@Inject(
			method = "tick",
			at = @At(
					value = "RETURN"
			)
	)
	private void endTickTaskEntitiesAndTickWorld(BooleanSupplier isAheadOfTime, CallbackInfo ci) {
		endTickTaskRSMM();
		endTickTaskRSMM();
	}
	
	@Inject(
			method = "tickTime",
			at = @At(
					value = "HEAD"
			)
	)
	private void beforeTickTime(CallbackInfo ci) {
		if (field_25143) {
			swapTickTaskRSMM(TickTask.TICK_TIME);
		}
	}
	
	@Inject(
			method = "tickTime",
			at = @At(
					value = "RETURN"
			)
	)
	private void onTickTime(CallbackInfo ci) {
		if (field_25143) {
			getMultimeterServer().onOverworldTickTime();
		}
	}
	
	@Inject(
			method = "method_29202",
			at = @At(
					value = "HEAD"
			)
	)
	private void startTickTaskCustomMobSpawning(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
		startTickTaskRSMM(TickTask.CUSTOM_MOB_SPAWNING);
	}
	
	@Inject(
			method = "method_29202",
			at = @At(
					value = "RETURN"
			)
	)
	private void endTickTaskCustomMobSpawning(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
		endTickTaskRSMM();
	}
	
	@Inject(
			method = "wakeSleepingPlayers",
			at = @At(
					value = "HEAD"
			)
	)
	private void startTickTaskWakeSleepingPlayers(CallbackInfo ci) {
		startTickTaskRSMM(TickTask.WAKE_SLEEPING_PLAYERS);
	}
	
	@Inject(
			method = "wakeSleepingPlayers",
			at = @At(
					value = "RETURN"
			)
	)
	private void endTickTaskWakeSleepingPlayers(CallbackInfo ci) {
		endTickTaskRSMM();
	}
	
	@Inject(
			method = "tickChunk",
			at = @At(
					value = "HEAD"
			)
	)
	private void startTickTaskTickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		startTickTaskRSMM(false, TickTask.TICK_CHUNK);
	}
	
	@Inject(
			method = "tickChunk",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
					args = "ldc=thunder"
			)
	)
	private void startTickTaskThunder(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		startTickTaskRSMM(false, TickTask.THUNDER);
	}
	
	@Inject(
			method = "tickChunk",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=iceandsnow"
			)
	)
	private void swapTickTaskPrecipitation(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		swapTickTaskRSMM(false, TickTask.PRECIPITATION);
	}
	
	@Inject(
			method = "tickChunk",
			at = @At(
					value = "INVOKE_STRING",
					target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
					args = "ldc=tickBlocks"
			)
	)
	private void swapTickTaskRandomTicks(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		swapTickTaskRSMM(false, TickTask.RANDOM_TICKS);
	}
	
	@Inject(
			method = "tickChunk",
			at = @At(
					value = "RETURN"
			)
	)
	private void endTickTaskRandomTicksAndTickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		endTickTaskRSMM(false);
		endTickTaskRSMM(false);
	}
	
	@Inject(
			method = "tickFluid",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/fluid/FluidState;onScheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"
			)
	)
	private void onTickFluid(ScheduledTick<Fluid> scheduledTick, CallbackInfo ci) {
		getMultimeter().logScheduledTick((World)(Object)this, scheduledTick);
	}
	
	@Inject(
			method = "tickBlock",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V"
			)
	)
	private void onTickBlock(ScheduledTick<Block> scheduledTick, CallbackInfo ci) {
		getMultimeter().logScheduledTick((World)(Object)this, scheduledTick);
	}
	
	@Inject(
			method = "tickEntity",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/entity/Entity;tick()V"
			)
	)
	private void onTickEntity(Entity entity, CallbackInfo ci) {
		getMultimeter().logEntityTick((World)(Object)this, entity);
	}
	
	@Inject(
			method = "processSyncedBlockEvents",
			at = @At(
					value = "HEAD"
			)
	)
	private void onProcessBlockEvents(CallbackInfo ci) {
		currentDepth = 0;
		currentBatch = syncedBlockEventQueue.size();
	}
	
	@Inject(
			method = "processSyncedBlockEvents",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/ObjectLinkedOpenHashSet;isEmpty()Z"
			)
	)
	private void onNextBlockEvent(CallbackInfo ci) {
		if (currentBatch == 0) {
			currentDepth++;
			currentBatch = syncedBlockEventQueue.size();
		}
		
		currentBatch--;
	}
	
	@Inject(
			method = "processBlockEvent",
			at = @At(
					value = "INVOKE",
					shift = Shift.BEFORE,
					target = "Lnet/minecraft/block/BlockState;onSyncedBlockEvent(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z"
			)
	)
	private void onProcessBlockEvent(BlockEvent blockEvent, CallbackInfoReturnable<Boolean> cir) {
		getMultimeter().logBlockEvent((World)(Object)this, blockEvent, currentDepth);
	}
	
	@Override
	public MultimeterServer getMultimeterServer() {
		return ((IMinecraftServer)server).getMultimeterServer();
	}
}