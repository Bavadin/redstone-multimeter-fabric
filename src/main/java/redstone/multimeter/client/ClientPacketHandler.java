package redstone.multimeter.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

import redstone.multimeter.common.network.PacketHandler;
import redstone.multimeter.common.network.RSMMPacket;

public class ClientPacketHandler extends PacketHandler {
	
	private final MultimeterClient client;
	
	public ClientPacketHandler(MultimeterClient client) {
		this.client = client;
	}
	
	@Override
	protected Packet<?> toCustomPayload(Identifier id, PacketByteBuf buffer) {
		return new CustomPayloadC2SPacket(id, buffer);
	}
	
	public void send(RSMMPacket packet) {
		client.getMinecraftClient().getNetworkHandler().sendPacket(encode(packet));
	}
	
	public void onPacketReceived(PacketByteBuf buffer) {
		try {
			decode(buffer).execute(client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			buffer.release();
		}
	}
}
