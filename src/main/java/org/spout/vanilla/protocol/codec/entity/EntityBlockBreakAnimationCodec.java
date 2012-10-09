package org.spout.vanilla.protocol.codec.entity;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.entity.EntityBlockBreakAnimationMessage;

public class EntityBlockBreakAnimationCodec extends MessageCodec<EntityBlockBreakAnimationMessage> {

	public EntityBlockBreakAnimationCodec() {
		super(EntityBlockBreakAnimationMessage.class, 0x37);
	}
	
	@Override
	public EntityBlockBreakAnimationMessage decode(ChannelBuffer buffer)
			throws IOException {
		int id = buffer.readInt();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		byte destroyStage = buffer.readByte();
		return new EntityBlockBreakAnimationMessage(id, x, y, z, destroyStage);
	}
	
	@Override
	public ChannelBuffer encode(EntityBlockBreakAnimationMessage message)
			throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(17);
		buffer.writeInt(message.getEntityId());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getDestroyStage());
		return buffer;
	}
	
}
