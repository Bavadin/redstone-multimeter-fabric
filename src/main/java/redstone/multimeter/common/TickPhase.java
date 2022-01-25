package redstone.multimeter.common;

import java.util.Arrays;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.Tag;

import redstone.multimeter.client.gui.Tooltip;
import redstone.multimeter.util.NbtUtils;
import redstone.multimeter.util.TextUtils;

public class TickPhase {
	
	public static final TickPhase UNKNOWN = new TickPhase(TickTask.UNKNOWN);
	
	private final TickTask[] tasks;
	
	public TickPhase(TickTask... tasks) {
		this.tasks = tasks;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TickPhase)) {
			return false;
		}
		
		return Arrays.equals(tasks, ((TickPhase)obj).tasks);
	}
	
	@Override
	public String toString() {
		String string = tasks[0].getName();
		
		for (int index = 1; index < tasks.length; index++) {
			string += " > " + tasks[index].getName();
		}
		
		return string;
	}
	
	public void addTextToTooltip(Tooltip tooltip) {
		tooltip.add(TextUtils.formatFancyText("tick phase", tasks[0].getName()));
		
		// used to indent subsequent lines
		String whitespace = "              ";
		
		for (int index = 1; index < tasks.length; index++) {
			tooltip.add(whitespace + "> " + tasks[index].getName());
			whitespace += "  ";
		}
	}
	
	public TickPhase startTask(TickTask task) {
		if (this == UNKNOWN || tasks.length == 0) {
			return new TickPhase(task);
		}
		
		TickTask[] array = new TickTask[tasks.length + 1];
		
		for (int index = 0; index < tasks.length; index++) {
			array[index] = tasks[index];
		}
		array[tasks.length] = task;
		
		return new TickPhase(array);
	}
	
	public TickPhase endTask() {
		if (this == UNKNOWN || tasks.length == 1) {
			return UNKNOWN;
		}
		
		TickTask[] array = new TickTask[tasks.length - 1];
		
		for (int index = 0; index < array.length; index++) {
			array[index] = tasks[index];
		}
		
		return new TickPhase(array);
	}
	
	public TickPhase swapTask(TickTask task) {
		if (this == UNKNOWN || tasks.length == 1) {
			return new TickPhase(new TickTask[] { task });
		}
		
		TickTask[] array = new TickTask[tasks.length];
		
		for (int index = 0; index < tasks.length; index++) {
			array[index] = tasks[index];
		}
		array[array.length - 1] = task;
		
		return new TickPhase(array);
	}
	
	public Tag toNbt() {
		if (this == UNKNOWN) {
			return NbtUtils.NULL;
		}
		
		byte[] array = new byte[tasks.length];
		
		for (int index = 0; index < array.length; index++) {
			array[index] = (byte)tasks[index].getIndex();
		}
		
		return new ByteArrayTag(array);
	}
	
	public static TickPhase fromNbt(Tag nbt) {
		if (nbt.getType() != NbtUtils.TYPE_BYTE_ARRAY) {
			return UNKNOWN;
		}
		
		ByteArrayTag nbtArray = (ByteArrayTag)nbt;
		byte[] array = nbtArray.getByteArray();
		TickTask[] tasks = new TickTask[array.length];
		
		for (int index = 0; index < tasks.length; index++) {
			tasks[index] = TickTask.fromIndex(array[index]);
		}
		
		return new TickPhase(tasks);
	}
}
