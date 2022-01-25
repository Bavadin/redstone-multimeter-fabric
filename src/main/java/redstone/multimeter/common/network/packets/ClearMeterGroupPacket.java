package redstone.multimeter.common.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;

import redstone.multimeter.client.MultimeterClient;
import redstone.multimeter.common.network.RSMMPacket;
import redstone.multimeter.server.MultimeterServer;

public class ClearMeterGroupPacket implements RSMMPacket {
	
	public ClearMeterGroupPacket() {
		
	}
	
	@Override
	public void encode(CompoundTag data) {
		
	}
	
	@Override
	public void decode(CompoundTag data) {
		
	}
	
	@Override
	public void execute(MultimeterServer server, ServerPlayerEntity player) {
		
	}
	
	@Override
	public void execute(MultimeterClient client) {
		client.getMeterGroup().clear();
	}
}
