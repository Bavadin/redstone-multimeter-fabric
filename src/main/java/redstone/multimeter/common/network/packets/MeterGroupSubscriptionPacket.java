package redstone.multimeter.common.network.packets;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import redstone.multimeter.client.MultimeterClient;
import redstone.multimeter.client.meter.ClientMeterGroup;
import redstone.multimeter.common.network.RSMMPacket;
import redstone.multimeter.server.Multimeter;
import redstone.multimeter.server.MultimeterServer;
import redstone.multimeter.server.meter.ServerMeterGroup;

public class MeterGroupSubscriptionPacket implements RSMMPacket {
	
	private String name;
	private boolean subscribed;
	
	public MeterGroupSubscriptionPacket() {
		
	}
	
	public MeterGroupSubscriptionPacket(String name, boolean subscribed) {
		this.name = name;
		this.subscribed = subscribed;
	}
	
	@Override
	public void encode(NbtCompound data) {
		data.putString("name", name);
		data.putBoolean("subscribed", subscribed);
	}
	
	@Override
	public void decode(NbtCompound data) {
		name = data.getString("name");
		subscribed = data.getBoolean("subscribed");
	}
	
	@Override
	public void execute(MultimeterServer server, ServerPlayerEntity player) {
		Multimeter multimeter = server.getMultimeter();
		ServerMeterGroup meterGroup = multimeter.getMeterGroup(name);
		
		if (subscribed) {
			if (meterGroup == null) {
				multimeter.createMeterGroup(player, name);
			} else {
				multimeter.subscribeToMeterGroup(meterGroup, player);
			}
		} else {
			if (meterGroup == null) {
				multimeter.refreshMeterGroup(player);
			} else {
				multimeter.unsubscribeFromMeterGroup(meterGroup, player);
			}
		}
	}
	
	@Override
	public void execute(MultimeterClient client) {
		ClientMeterGroup meterGroup = client.getMeterGroup();
		
		if (subscribed) {
			meterGroup.subscribe(name);
		} else {
			meterGroup.unsubscribe(false);
		}
	}
}
