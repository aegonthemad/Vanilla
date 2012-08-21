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
package org.spout.vanilla.protocol.handler;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.player.Player;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.util.Parameter;
import org.spout.vanilla.controller.living.Living;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.entity.EntityActionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;

public final class EntityActionMessageHandler implements ServerMessageHandler<EntityActionMessage> {
	@Override
	public void handle(Session session, EntityActionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();

		if (!(player.getController() instanceof Living)) {
			return;
		}

		VanillaPlayer ve = (VanillaPlayer) player.getController();
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();

		switch (message.getAction()) {
			case EntityActionMessage.ACTION_CROUCH:
				parameters.add(EntityMetadataMessage.Parameters.META_CROUCHED.get());
				session.send(false, new EntityMetadataMessage(player.getId(), parameters));
				break;
			case EntityActionMessage.ACTION_UNCROUCH:
				parameters.add(EntityMetadataMessage.Parameters.META_CROUCHED.get());
				session.send(false, new EntityMetadataMessage(player.getId(), parameters));
				break;
			case EntityActionMessage.ACTION_LEAVE_BED:
				session.send(false, new EntityActionMessage(player.getId(), EntityActionMessage.ACTION_LEAVE_BED));
				break;
			case EntityActionMessage.ACTION_START_SPRINTING:
				parameters.add(EntityMetadataMessage.Parameters.META_SPRINTING.get());
				session.send(false, new EntityMetadataMessage(player.getId(), parameters));
				ve.setSprinting(true);
				break;
			case EntityActionMessage.ACTION_STOP_SPRINTING:
				parameters.add(EntityMetadataMessage.Parameters.META_SPRINTING.get());
				session.send(false, new EntityMetadataMessage(player.getId(), parameters));
				ve.setSprinting(false);
				break;
			default:
				break;
		}
	}
}
