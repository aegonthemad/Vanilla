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

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.player.Player;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.VanillaEntityController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.protocol.msg.entity.EntityInteractionMessage;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class EntityInteractionMessageHandler implements ServerMessageHandler<EntityInteractionMessage> {
	@Override
	public void handle(Session session, EntityInteractionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();
		Entity clickedEntity = player.getWorld().getEntity(message.getTarget());
		if (clickedEntity == null) {
			return;
		}

		ItemStack holding = VanillaPlayerUtil.getCurrentItem(player);
		Material holdingMat = holding == null ? VanillaMaterials.AIR : holding.getMaterial();
		if (holdingMat == null) {
			holdingMat = VanillaMaterials.AIR;
		}
		if (message.isPunching()) {
			VanillaPlayer vPlayer = (VanillaPlayer) player.getController();
			holdingMat.onInteract(player, clickedEntity, Action.LEFT_CLICK);
			clickedEntity.getController().onInteract(player, Action.LEFT_CLICK);

			if (clickedEntity.getController() instanceof VanillaPlayer && !VanillaConfiguration.PLAYER_PVP_ENABLED.getBoolean()) {
				return;
			}

			if (clickedEntity.getController() instanceof VanillaEntityController) {
				VanillaEntityController damaged = (VanillaEntityController) clickedEntity.getController();
				if (clickedEntity.getController() instanceof VanillaPlayer && (!vPlayer.isSurvival() || !VanillaPlayerUtil.isSurvival(damaged.getParent()))) {
					return;
				}
				vPlayer.getSurvivalLogic().addExhaustion(ExhaustionLevel.ATTACK_ENEMY.getAmount());

				if (clickedEntity.getController() instanceof VanillaPlayer) {
					((VanillaPlayer) clickedEntity.getController()).getSurvivalLogic().addExhaustion(ExhaustionLevel.RECEIVE_DAMAGE.getAmount());
				}

				int damage = 1;
				if (holding != null && holdingMat instanceof VanillaMaterial) {
					damage = ((VanillaMaterial) holdingMat).getDamage();
					if (holdingMat instanceof Tool) {
						// This is a bit of a hack due to the way Tool hierarchy is now (Only Swords can have a damage modifier, but Sword must be an interface and therefore is not able to contain getDamageModifier without code duplication)
						damage += ((Tool) holdingMat).getDamageBonus(damaged, holding);
						vPlayer.getInventory().getQuickbar().getCurrentSlotInventory().addItemData(1);
					}
				}
				if (damage != 0) {
					if (!damaged.getParent().isDead()) {
						damaged.damage(damage, DamageCause.ATTACK, vPlayer, damage > 0);
					}
				}
			}
		} else {
			holdingMat.onInteract(player, clickedEntity, Action.RIGHT_CLICK);
			clickedEntity.getController().onInteract(player, Action.RIGHT_CLICK);
		}
	}
}
