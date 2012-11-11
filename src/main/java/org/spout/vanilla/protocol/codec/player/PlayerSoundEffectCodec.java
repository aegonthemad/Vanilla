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
package org.spout.vanilla.protocol.codec.player;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.player.PlayerSoundEffectMessage;

public final class PlayerSoundEffectCodec extends MessageCodec<PlayerSoundEffectMessage> {
	public PlayerSoundEffectCodec() {
		super(PlayerSoundEffectMessage.class, 0x3e);
	}

	/*
	 * Note to the people that like to move the mathematics involved:
	 * THIS IS PART OF ENCODING AND DECODING
	 * This task is not part of the one actually wishing to send or handle a message.
	 * So don't move this component. Thank you.
	 */

	@Override
	public PlayerSoundEffectMessage decode(ChannelBuffer buffer) throws IOException {
		String soundName = ChannelBufferUtils.readString(buffer);
		float x = (float) buffer.readInt() / 8.0f;
		float y = (float) buffer.readInt() / 8.0f;
		float z = (float) buffer.readInt() / 8.0f;
		float volume = buffer.readFloat();
		float pitch = 63f / (float) buffer.readUnsignedByte();
		return new PlayerSoundEffectMessage(soundName, x, y, z, volume, pitch);
	}

	@Override
	public ChannelBuffer encode(PlayerSoundEffectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeString(buffer, message.getSoundName());
		buffer.writeInt((int) (message.getX() * 8.0f));
		buffer.writeInt((int) (message.getY() * 8.0f));
		buffer.writeInt((int) (message.getZ() * 8.0f));
		buffer.writeFloat(message.getVolume());
		buffer.writeByte((byte) (message.getPitch() * 63f));
		return buffer;
	}
}
