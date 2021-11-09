package redstone.multimeter.common.meter;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import redstone.multimeter.common.WorldPos;
import redstone.multimeter.common.meter.event.EventType;
import redstone.multimeter.common.meter.log.MeterLogs;

public class Meter {
	
	private static final AtomicLong ID_COUNTER = new AtomicLong(0);
	
	private final long id;
	private final MeterProperties properties;
	private final MeterLogs logs;
	
	/** true if the block at this position is receiving power */
	private boolean powered;
	/** true if the block at this position is emitting power or active in another way */
	private boolean active;
	
	/** This property is used on the client to hide a meter in the HUD */
	private boolean hidden;
	
	public Meter(long id, MeterProperties properties) {
		this.id = id;
		this.properties = properties;
		this.logs = new MeterLogs();
	}
	
	public Meter(MeterProperties properties) {
		this(ID_COUNTER.getAndIncrement(), properties);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Meter) {
			Meter meter = (Meter)obj;
			return meter.id == id;
		}
		
		return false;
	}
	
	public long getId() {
		return id;
	}
	
	public MeterProperties getProperties() {
		return properties;
	}
	
	public MeterLogs getLogs() {
		return logs;
	}
	
	public void applyUpdate(Consumer<MeterProperties> update) {
		update.accept(properties);
	}
	
	public WorldPos getPos() {
		return properties.getPos();
	}
	
	public boolean isIn(World world) {
		return properties.getPos().isOf(world);
	}
	
	public String getName() {
		return properties.getName();
	}
	
	public int getColor() {
		return properties.getColor();
	}
	
	public boolean isMovable() {
		return properties.getMovable();
	}
	
	public int getEventTypes() {
		return properties.getEventTypes();
	}
	
	public boolean isMetering(EventType type) {
		return properties.hasEventType(type);
	}
	
	public boolean isPowered() {
		return powered;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean setPowered(boolean powered) {
		boolean wasPowered = this.powered;
		this.powered = powered;
		
		return wasPowered != powered;
	}
	
	public boolean setActive(boolean active) {
		boolean wasActive = this.active;
		this.active = active;
		
		return wasActive != active;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void toggleHidden() {
		setHidden(!hidden);
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public NbtCompound toNBT() {
		NbtCompound nbt = new NbtCompound();
		
		nbt.putLong("id", id);
		nbt.put("properties", properties.toNBT());
		nbt.putBoolean("powered", powered);
		nbt.putBoolean("active", active);
		
		return nbt;
	}
	
	public static Meter fromNBT(NbtCompound nbt) {
		long id = nbt.getLong("id");
		MeterProperties properties = MeterProperties.fromNBT(nbt.getCompound("properties"));
		boolean powered = nbt.getBoolean("powered");
		boolean active = nbt.getBoolean("active");
		
		Meter meter = new Meter(id, properties);
		meter.setPowered(powered);
		meter.setActive(active);
		
		return meter;
	}
}
