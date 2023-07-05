package redstone.multimeter.common.network.packets;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import redstone.multimeter.client.MultimeterClient;
import redstone.multimeter.common.network.RSMMPacket;
import redstone.multimeter.server.MultimeterServer;

public class MeterGroupDefaultPacket implements RSMMPacket {

	public MeterGroupDefaultPacket() {
	}

	@Override
	public void encode(NbtCompound data) {
	}

	@Override
	public void decode(NbtCompound data) {
	}

	@Override
	public void handle(MultimeterServer server, ServerPlayerEntity player) {
	}

	@Override
	public void handle(MultimeterClient client) {
		client.createDefaultMeterGroup();
	}
}
