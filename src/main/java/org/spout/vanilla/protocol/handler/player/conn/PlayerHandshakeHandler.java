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
package org.spout.vanilla.protocol.handler.player.conn;

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.security.SecurityHandler;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerHandshakeMessage;
import org.spout.vanilla.protocol.netcache.ChunkNetCache;

<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/handler/bootstrap/BootstrapHandshakeMessageHandler.java
public class BootstrapHandshakeMessageHandler implements ServerMessageHandler<HandshakeMessage> {
	@Override
	public void handle(Session session, HandshakeMessage message) {
=======
public class PlayerHandshakeHandler extends MessageHandler<PlayerHandshakeMessage> {
	@Override
	public void handleServer(Session session, PlayerHandshakeMessage message) {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/handler/player/conn/PlayerHandshakeHandler.java
		if (message.getProtocolVersion() < VanillaPlugin.MINECRAFT_PROTOCOL_ID) {
			session.disconnect(VanillaConfiguration.OUTDATED_CLIENT_MESSAGE.getString());
			return;
		} else if (message.getProtocolVersion() > VanillaPlugin.MINECRAFT_PROTOCOL_ID) {
			session.disconnect(VanillaConfiguration.OUTDATED_SERVER_MESSAGE.getString());
			return;
		}
		session.getDataMap().put(VanillaProtocol.CHUNK_NET_CACHE, new ChunkNetCache());
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			session.getDataMap().put(VanillaProtocol.LOGIN_TIME, System.currentTimeMillis());
			session.getDataMap().put(VanillaProtocol.HANDSHAKE_USERNAME, message.getUsername());
			session.setState(Session.State.EXCHANGE_ENCRYPTION);
			final String sessionId;
			if (VanillaConfiguration.ONLINE_MODE.getBoolean()) {
				sessionId = getSessionId();
			} else {
				sessionId = "-";
			}
			session.getDataMap().put(VanillaProtocol.SESSION_ID, sessionId);
			int keySize = VanillaConfiguration.ENCRYPT_KEY_SIZE.getInt();
			String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
			AsymmetricCipherKeyPair keys = SecurityHandler.getInstance().getKeyPair(keySize, keyAlgorithm);
			byte[] randombyte = new byte[4];
			random.nextBytes(randombyte);
			session.getDataMap().put("verifytoken", randombyte);
			byte[] secret = SecurityHandler.getInstance().encodeKey(keys.getPublic());
			session.send(true, new EncryptionKeyRequestMessage(sessionId, false, secret, randombyte));
		} else {
			session.disconnect(false, new Object[]{"Handshake already exchanged."});
		}
	}

	private final static SecureRandom random = new SecureRandom();

	static {
		synchronized (random) {
			random.nextBytes(new byte[1]);
		}
	}

	private final String getSessionId() {
		long sessionId;
		synchronized (random) {
			sessionId = random.nextLong();
		}
		StringBuilder sb = new StringBuilder();
		if (sessionId < 0) {
			sessionId = (-sessionId) & 0x7FFFFFFFFFFFFFFFL;
			sb.append("-");
		}
		return sb.append(Long.toString(sessionId, 16)).toString();
	}
}
