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

import org.spout.api.entity.Player;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/handler/HeldItemChangeMessageHandler.java
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.protocol.msg.HeldItemChangeMessage;

public final class HeldItemChangeMessageHandler implements ServerMessageHandler<HeldItemChangeMessage> {
	@Override
	public void handle(Session session, HeldItemChangeMessage message) {
=======

import org.spout.vanilla.component.player.PingComponent;
import org.spout.vanilla.protocol.msg.player.conn.PlayerPingMessage;

public class PlayerPingHandler extends MessageHandler<PlayerPingMessage> {
	@Override
	public void handleServer(Session session, PlayerPingMessage message) {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/handler/player/conn/PlayerPingHandler.java
		if (!session.hasPlayer()) {
			session.disconnect("Illegal packet!");
			return;
		}
		Player player = session.getPlayer();
		if (player.has(PingComponent.class)) {
			player.get(PingComponent.class).response(message.getPingId());
		}
	}
}
