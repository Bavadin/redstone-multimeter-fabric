package redstone.multimeter.block;

import net.minecraft.world.World;

import redstone.multimeter.interfaces.mixin.IBlock;

public interface PowerSource extends IBlock {
	
	public static final int MIN_POWER = 0;
	public static final int MAX_POWER = 15;
	
	@Override
	default boolean isPowerSource() {
		return true;
	}
	
	default boolean logPowerChangeOnStateChange() {
		return true;
	}
	
	public int getPowerLevel(World world, int x, int y, int z, int metadata);
	
}
