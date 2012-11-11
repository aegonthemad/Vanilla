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
package org.spout.vanilla.protocol.codec.entity.spawn;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.nbt.CompoundMap;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/entity/EntitySpawnItemCodec.java
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnItemMessage;
=======
import org.spout.nbt.CompoundTag;
import org.spout.nbt.StringTag;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityItemMessage;
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/entity/spawn/EntityItemCodec.java

public final class EntityItemCodec extends MessageCodec<EntityItemMessage> {
	public EntityItemCodec() {
		super(EntityItemMessage.class, 0x15);
	}

	@Override
	public EntityItemMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
<<<<<<< HEAD
		int itemId = buffer.readUnsignedShort();
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/entity/EntitySpawnItemCodec.java
		int count = buffer.readUnsignedByte();
		int damage = buffer.readUnsignedShort();
		CompoundMap nbtTags = ChannelBufferUtils.readCompound(buffer);
=======
		CompoundMap item = ChannelBufferUtils.readCompound(buffer);
>>>>>>> 5eefa5ca2f82ce4c9f10cce05a3b20421643a279
=======
		int count = 1;
		int damage = 0;
		if (itemId != -1) {
			count = buffer.readUnsignedByte();
			damage = buffer.readUnsignedShort();
		}
		buffer.markReaderIndex();
		int strLen = buffer.readShort();
		if (strLen != -1) {
			buffer.resetReaderIndex();
			//TODO: NBT Data?
			ChannelBufferUtils.readCompound(buffer);
		}
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/entity/spawn/EntityItemCodec.java
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int rotation = buffer.readUnsignedByte();
		int pitch = buffer.readUnsignedByte();
		int roll = buffer.readUnsignedByte();
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/entity/EntitySpawnItemCodec.java
<<<<<<< HEAD
		return new EntitySpawnItemMessage(id, itemId, count, (short) damage, nbtTags, x, y, z, rotation, pitch, roll);
=======
		return new EntitySpawnItemMessage(id, item, x, y, z, rotation, pitch, roll);
>>>>>>> 5eefa5ca2f82ce4c9f10cce05a3b20421643a279
	}

	@Override
	public ChannelBuffer encode(EntitySpawnItemMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getEntityId());
<<<<<<< HEAD
		buffer.writeShort(message.getItemId());
		buffer.writeByte(message.getCount());
		buffer.writeShort(message.getDamage());
		ChannelBufferUtils.writeCompound(buffer, message.getNBTTags());
=======
		ChannelBufferUtils.writeCompound(buffer, message.getItem());
>>>>>>> 5eefa5ca2f82ce4c9f10cce05a3b20421643a279
=======
		return new EntityItemMessage(id, itemId, count, (short) damage, x, y, z, rotation, pitch, roll);
	}

	@Override
	public ChannelBuffer encode(EntityItemMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getEntityId());
		buffer.writeShort(message.getId());
		buffer.writeByte(message.getCount());
		buffer.writeShort(message.getDamage());
		buffer.writeShort(-1); //TODO: NBT data?
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/entity/spawn/EntityItemCodec.java
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getRotation());
		buffer.writeByte(message.getPitch());
		buffer.writeByte(message.getRoll());
		return buffer;
	}
}
