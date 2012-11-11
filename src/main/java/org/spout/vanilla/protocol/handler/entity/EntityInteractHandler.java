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
package org.spout.vanilla.protocol.handler.entity;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/handler/EntityInteractionMessageHandler.java
=======

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.living.LivingComponent;
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/handler/entity/EntityInteractHandler.java
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.protocol.msg.entity.EntityInteractMessage;
import org.spout.vanilla.source.DamageCause;

<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/handler/EntityInteractionMessageHandler.java
public class EntityInteractionMessageHandler implements ServerMessageHandler<EntityInteractionMessage> {
	@Override
	public void handle(Session session, EntityInteractionMessage message) {
=======
public class EntityInteractHandler extends MessageHandler<EntityInteractMessage> {
	@Override
	public void handleServer(Session session, EntityInteractMessage message) {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/handler/entity/EntityInteractHandler.java
		if (!session.hasPlayer()) {
			return;
		}

		Player playerEnt = session.getPlayer();
		Human player = playerEnt.get(Human.class);
		Entity clickedEntity = playerEnt.getWorld().getEntity(message.getTarget());
		if (clickedEntity == null || player == null) {
			return;
		}

		ItemStack holding = player.getInventory().getQuickbar().getCurrentItem();
		Material holdingMat = holding == null ? VanillaMaterials.AIR : holding.getMaterial();
		if (holdingMat == null) {
			holdingMat = VanillaMaterials.AIR;
		}
		if (message.isPunching()) {
			holdingMat.onInteract(playerEnt, clickedEntity, Action.LEFT_CLICK);
			clickedEntity.interact(Action.LEFT_CLICK, playerEnt);

			if (clickedEntity.has(Human.class) && !VanillaConfiguration.PLAYER_PVP_ENABLED.getBoolean()) {
				return;
			}

			LivingComponent clicked = clickedEntity.get(LivingComponent.class);
			if (clicked != null) {
				//TODO: Reimplement exhaustion values

				int damage = 1;
				if (holding != null && holdingMat instanceof VanillaMaterial) {
					damage = ((VanillaMaterial) holdingMat).getDamage();
					if (holdingMat instanceof Tool) {
						// This is a bit of a hack due to the way Tool hierarchy is now (Only Swords can have a damage modifier, but Sword must be an interface and therefore is not able to contain getDamageModifier without code duplication)
						damage += ((Tool) holdingMat).getDamageBonus(clickedEntity, holding);
						//						player.getInventory().getQuickbar().getCurrentSlotInventory().addData(1); TODO: Reimplement durability change
					}
				}
				if (damage != 0) {
					if (clicked instanceof Human) {
						if (((Human) clicked).getGameMode() != GameMode.SURVIVAL) {
							return;
						}
					}
					if (!clicked.getHealth().isDead()) {
						clicked.getHealth().damage(damage, DamageCause.ATTACK, playerEnt, damage > 0);
					}
				}
			}
		} else {
			holdingMat.onInteract(playerEnt, clickedEntity, Action.RIGHT_CLICK);
			clickedEntity.interact(Action.RIGHT_CLICK, playerEnt);
		}
	}
}
