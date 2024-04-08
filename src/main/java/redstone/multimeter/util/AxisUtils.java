package redstone.multimeter.util;

import net.minecraft.util.math.Direction.Axis;

public class AxisUtils {

	public static int choose(Axis axis, int x, int y, int z) {
		switch (axis) {
		case X:
			return x;
		case Y:
			return y;
		case Z:
			return z;
		}

		return 0;
	}
}