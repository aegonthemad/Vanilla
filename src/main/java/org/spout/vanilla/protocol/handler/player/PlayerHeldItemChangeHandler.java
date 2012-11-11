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
package org.spout.vanilla.protocol.handler.player;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/handler/EntityHeadYawMessageHandler.java
import org.spout.api.math.MathHelper;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.entity.component.HeadOwner;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.EntityHeadYawMessage;

public class EntityHeadYawMessageHandler implements ServerMessageHandler<EntityHeadYawMessage> {
	@Override
	public void handle(Session session, EntityHeadYawMessage message) {
=======
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.player.HUDComponent;
import org.spout.vanilla.event.entity.EntityEquipmentEvent;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.protocol.msg.player.PlayerHeldItemChangeMessage;

public final class PlayerHeldItemChangeHandler extends MessageHandler<PlayerHeldItemChangeMessage> {
	@Override
	public void handleServer(Session session, PlayerHeldItemChangeMessage message) {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/handler/player/PlayerHeldItemChangeHandler.java
		if (!session.hasPlayer()) {
			return;
		}
		Human human = session.getPlayer().get(Human.class);
		if (human == null) {
			return;
		}
		int newSlot = message.getSlot();
		if (newSlot < 0 || newSlot > 8) {
			return;
		}
		PlayerQuickbar quickbar = human.getInventory().getQuickbar();
		quickbar.setCurrentSlot(newSlot);
		ItemStack item = quickbar.getCurrentItem();
		Player player = session.getPlayer();
		player.getNetwork().callProtocolEvent(new EntityEquipmentEvent(player, 0, item));
	}
	
	@Override
	public void handleClient(Session session, PlayerHeldItemChangeMessage message) {
		Player player = ((Client) Spout.getEngine()).getActivePlayer();
		player.get(HUDComponent.class).setHotbarSlot(message.getSlot());
	}
}
