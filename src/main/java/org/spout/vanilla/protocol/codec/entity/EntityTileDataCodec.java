/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.codec.entity;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.nbt.CompoundMap;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/TileEntityDataCodec.java
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.TileEntityDataMessage;
=======
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/entity/EntityTileDataCodec.java

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.EntityTileDataMessage;

public class EntityTileDataCodec extends MessageCodec<EntityTileDataMessage> {
	public EntityTileDataCodec() {
		super(EntityTileDataMessage.class, 0x84);
	}

	@Override
	public EntityTileDataMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readShort();
		int z = buffer.readInt();
		int action = buffer.readByte();
		CompoundMap data = ChannelBufferUtils.readCompound(buffer);
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/TileEntityDataCodec.java
		return new TileEntityDataMessage(x, y, z, action, data);
	}

	@Override
	public ChannelBuffer encode(TileEntityDataMessage message) throws IOException {
=======
		return new EntityTileDataMessage(x, y, z, action, data);
	}

	@Override
	public ChannelBuffer encode(EntityTileDataMessage message) throws IOException {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/entity/EntityTileDataCodec.java
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getAction());
		ChannelBufferUtils.writeCompound(buffer, message.getData());
		return buffer;
	}
}