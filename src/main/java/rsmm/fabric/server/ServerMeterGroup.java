package rsmm.fabric.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

import rsmm.fabric.common.Meter;
import rsmm.fabric.common.MeterGroup;
import rsmm.fabric.common.WorldPos;
import rsmm.fabric.util.ColorUtils;

public class ServerMeterGroup extends MeterGroup {
	
	private final Multimeter multimeter;
	private final Set<ServerPlayerEntity> subscribers;
	private final ServerLogManager logManager;
	
	private int totalMeterCount; // The total number of meters ever added to this group
	
	public ServerMeterGroup(Multimeter multimeter, String name) {
		super(name);
		
		this.multimeter = multimeter;
		this.subscribers = new HashSet<>();
		this.logManager = new ServerLogManager(this);
	}
	
	@Override
	public void clear() {
		super.clear();
		
		totalMeterCount = 0;
	}
	
	@Override
	public void addMeter(Meter meter) {
		super.addMeter(meter);
		
		totalMeterCount++;
	}
	
	@Override
	public boolean removeMeter(Meter meter) {
		if (super.removeMeter(meter)) {
			if (getMeterCount() == 0) {
				totalMeterCount = 0;
			}
			
			return true;
		}
		
		return false;
	}
	
	public String getNextMeterName() {
		return String.format("Meter %d", totalMeterCount);
	}
	
	public int getNextMeterColor() {
		return ColorUtils.nextColor();
	}
	
	public Multimeter getMultimeter() {
		return multimeter;
	}
	
	public Set<ServerPlayerEntity> getSubscribers() {
		return Collections.unmodifiableSet(subscribers);
	}
	
	public boolean hasSubscribers() {
		return !subscribers.isEmpty();
	}
	
	public void addSubscriber(ServerPlayerEntity player) {
		subscribers.add(player);
	}
	
	public void removeSubscriber(ServerPlayerEntity player) {
		subscribers.remove(player);
	}
	
	public ServerLogManager getLogManager() {
		return logManager;
	}
	
	public void blockUpdate(WorldPos pos, boolean powered) {
		Meter meter = getMeterAt(pos);
		
		if (meter != null && meter.blockUpdate(powered)) {
			logManager.logPowered(meter, powered);
		}
	}
	
	public void stateChanged(WorldPos pos, boolean active) {
		Meter meter = getMeterAt(pos);
		
		if (meter != null && meter.stateChanged(active)) {
			logManager.logActive(meter, active);
		}
	}
	
	public void blockMoved(WorldPos pos, Direction dir) {
		Meter meter = getMeterAt(pos);
		
		if (meter != null) {
			if (!hasMeterAt(pos.offset(dir)) && meter.blockMoved(dir)) {
				int index = posToIndex.remove(pos);
				posToIndex.put(meter.getPos(), index);
			}
			
			logManager.logMoved(meter, dir);
		}
	}
}
