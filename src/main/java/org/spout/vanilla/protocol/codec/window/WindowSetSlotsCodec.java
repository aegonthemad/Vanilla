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
package org.spout.vanilla.protocol.codec.window;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.window.WindowSetSlotsMessage;

public final class WindowSetSlotsCodec extends MessageCodec<WindowSetSlotsMessage> {
	public WindowSetSlotsCodec() {
		super(WindowSetSlotsMessage.class, 0x68);
	}

	@Override
	public WindowSetSlotsMessage decode(ChannelBuffer buffer) throws IOException {
		byte id = buffer.readByte();
		short count = buffer.readShort();
		ItemStack[] items = new ItemStack[count];
		for (int slot = 0; slot < count; slot++) {
			items[slot] = ChannelBufferUtils.readItemStack(buffer);
		}
		return new WindowSetSlotsMessage(id, items);
	}

	@Override
	public ChannelBuffer encode(WindowSetSlotsMessage message) throws IOException {
		ItemStack[] items = message.getItems();
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getWindowInstanceId());
		buffer.writeShort(items.length);
		for (ItemStack item : items) {
			ChannelBufferUtils.writeItemStack(buffer, item);
		}
		return buffer;
	}
}
