package rsmm.fabric.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import rsmm.fabric.common.Meter;
import rsmm.fabric.common.WorldPos;
import rsmm.fabric.common.log.entry.LogEntry;
import rsmm.fabric.common.log.entry.LogType;

public class PacketUtils {
	
	public static final int MAX_STRING_LENGTH = 32767;
	
	public static void writeData(PacketByteBuf buffer, PacketByteBuf data) {
		int bytesCount = data.readableBytes();
		
		buffer.writeInt(bytesCount);
		buffer.writeBytes(data);
	}
	
	public static PacketByteBuf readData(PacketByteBuf buffer) {
		int bytesCount = buffer.readInt();
		PacketByteBuf data = new PacketByteBuf(buffer.readBytes(bytesCount));
		
		return data;
	}
	
	public static void writeWorldPos(PacketByteBuf buffer, WorldPos pos) {
		buffer.writeIdentifier(pos.getWorldId());
		buffer.writeBlockPos(pos);
	}
	
	public static WorldPos readWorldPos(PacketByteBuf buffer) {
		Identifier worldId = buffer.readIdentifier();
		BlockPos pos = buffer.readBlockPos();
		
		return new WorldPos(worldId, pos);
	}
	
	public static void writeMeter(PacketByteBuf buffer, Meter meter) {
		meter.encode(buffer);
	}
	
	public static Meter readMeter(PacketByteBuf buffer) {
		Meter meter = new Meter();
		meter.decode(buffer);
		
		return meter.getPos() == null ? null : meter;
	}
	
	public static void writeLogEntry(PacketByteBuf buffer, LogEntry<?> log) {
		buffer.writeString(log.getType().getName());
		
		log.encode(buffer);
	}
	
	public static LogEntry<?> readLogEntry(PacketByteBuf buffer) {
		LogType<?> type = LogType.fromName(buffer.readString(MAX_STRING_LENGTH));
		
		try {
			Class<? extends LogEntry<?>> entryType = type.entry();
			LogEntry<?> logEntry = entryType.getDeclaredConstructor(LogType.class).newInstance(type);
			
			logEntry.decode(buffer);
			
			return logEntry;
		} catch (Exception e) {
			
		}
		
		return null;
	}
}
