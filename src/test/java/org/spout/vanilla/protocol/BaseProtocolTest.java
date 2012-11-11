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
package org.spout.vanilla.protocol;

import java.io.IOException;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;

import org.spout.api.protocol.CodecLookupService;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.EngineFaker;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseProtocolTest {
	private final CodecLookupService codecLookup;
	private final Message[] testMessages;

	protected BaseProtocolTest(CodecLookupService codecLookup, Message[] testMessages) {
		EngineFaker.setupEngine();
		this.codecLookup = codecLookup;
		this.testMessages = testMessages;
	}

	@Test
	public void testMessageCodecLookup() {
		for (Message message : testMessages) {
			MessageCodec<?> codec = codecLookup.find(message.getClass());
			assertNotNull("Message " + message + " did not have a codec!", codec);
			MessageCodec<?> idCodec = codecLookup.find(codec.getOpcode());
			assertNotNull("No codec for opcode " + codec.getOpcode() + " in codec lookup!", idCodec);
		}
	}

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void testMessageEncoding() throws IOException {
		for (Message message : testMessages) {
			MessageCodec codec = codecLookup.find(message.getClass());
<<<<<<< HEAD
			ChannelBuffer encoded = codec.encode(message);
			Message decoded = codec.decode(encoded);
			assertEquals("Failed for: " + message.getClass().getName(), message, decoded);
=======
			ChannelBuffer encoded;
			Message decoded;
			try {
				encoded = codec.encodeToServer(message);
				decoded = codec.decodeFromClient(encoded);
			} catch (Throwable t) {
				fail("Failed (C -> S) for: " + message.getClass().getName() + ", " + message);
				return;
			}
			assertEquals("Failed (C -> S) for: " + message.getClass().getName(), message, decoded);
			try {
				encoded = codec.encodeToClient(message);
				decoded = codec.decodeFromServer(encoded);
			} catch (Throwable t) {
				fail("Failed (S -> C) for: " + message.getClass().getName() + ", " + message);
				return;
			}
			assertEquals("Failed (S -> C) for: " + message.getClass().getName(), message, decoded);
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42
		}
	}

	@Test
	public void testTestCompleteness() {
		final TIntSet testedOpcodes = new TIntHashSet();
		for (Message message : testMessages) {
			MessageCodec<?> codec = codecLookup.find(message.getClass());
			if (codec != null) {
				testedOpcodes.add(codec.getOpcode());
			}
		}
		for (MessageCodec<?> codec : codecLookup.getCodecs()) {
			assertTrue("Opcode " + codec.getOpcode() + " not tested", testedOpcodes.contains(codec.getOpcode()));
		}
	}
}
