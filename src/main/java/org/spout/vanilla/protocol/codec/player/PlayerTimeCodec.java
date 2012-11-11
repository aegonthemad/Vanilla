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

import org.spout.vanilla.protocol.msg.player.PlayerTimeMessage;

public final class PlayerTimeCodec extends MessageCodec<PlayerTimeMessage> {
	public PlayerTimeCodec() {
		super(PlayerTimeMessage.class, 0x04);
	}

	@Override
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/codec/TimeUpdateCodec.java
	public TimeUpdateMessage decode(ChannelBuffer buffer) throws IOException {
		long ageOfWorld = buffer.readLong();
		long timeOfDay = buffer.readLong();
        return new TimeUpdateMessage(ageOfWorld, timeOfDay);
	}

	@Override
	public ChannelBuffer encode(TimeUpdateMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(16);
		buffer.writeLong(message.getAgeOfWorld());
		buffer.writeLong(message.getTimeOfDay());
=======
	public PlayerTimeMessage decode(ChannelBuffer buffer) throws IOException {
		return new PlayerTimeMessage(buffer.readLong(), buffer.readLong());
	}

	@Override
	public ChannelBuffer encode(PlayerTimeMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(16);
		buffer.writeLong(message.getAge());
		buffer.writeLong(message.getTime());
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/codec/player/PlayerTimeCodec.java
		return buffer;
	}
}
