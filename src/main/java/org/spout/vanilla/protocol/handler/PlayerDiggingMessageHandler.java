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

import java.util.Collection;
import java.util.HashSet;

import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.Protection;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.basic.BasicAir;
import org.spout.api.material.block.BlockFace;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.protocol.ServerMessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.data.drops.flag.PlayerFlags;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.Food;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.protocol.msg.BlockChangeMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class PlayerDiggingMessageHandler implements ServerMessageHandler<PlayerDiggingMessage> {
	private void breakBlock(BlockMaterial blockMaterial, Block block, VanillaPlayerController player) {
		HashSet<Flag> flags = new HashSet<Flag>();
		if (player.isSurvival()) {
			flags.add(PlayerFlags.SURVIVAL);
		} else {
			flags.add(PlayerFlags.CREATIVE);
		}
		ItemStack heldItem = player.getInventory().getQuickbar().getCurrentItem();
		if (heldItem != null) {
			heldItem.getMaterial().getItemFlags(heldItem, flags);
		}
		blockMaterial.destroy(block, flags);
	}

	@Override
	public void handle(Session session, PlayerDiggingMessage message) {
		if (!session.hasPlayer()) {
			return;
		}

		Player player = session.getPlayer();

		int x = message.getX();
		int y = message.getY();
		int z = message.getZ();
		int state = message.getState();

		World w = player.getWorld();
		Point point = new Point(w, x, y, z);
		Block block = w.getBlock(point, player);
		BlockMaterial blockMaterial = block.getMaterial();

		short minecraftID = VanillaMaterials.getMinecraftId(blockMaterial);
		BlockFace clickedFace = message.getFace();

		VanillaPlayerController vp = ((VanillaPlayerController) player.getController());

		// Don't block protections if dropping an item, silly Notch...
		if (state != PlayerDiggingMessage.STATE_DROP_ITEM && state != PlayerDiggingMessage.STATE_SHOOT_ARROW_EAT_FOOD) {
			Collection<Protection> protections = Spout.getEngine().getServiceManager().getRegistration(ProtectionService.class).getProvider().getAllProtections(point);
			for (Protection p : protections) {
				if (p.contains(point) && !vp.isOp()) {
					player.getSession().send(false, new BlockChangeMessage(x, y, z, minecraftID, block.getData() & 0xF));
					player.sendMessage(ChatStyle.DARK_RED, "This area is a protected spawn point!");
					return;
				}
			}
		}

		if (state == PlayerDiggingMessage.STATE_DROP_ITEM && x == 0 && y == 0 && z == 0) {
			((VanillaPlayerController) player.getController()).dropItem();
			return;
		}

		boolean isInteractable = true;
		// FIXME: How so not interactable? I am pretty sure I can interact with water to place a boat, no?
		if (blockMaterial == VanillaMaterials.AIR || blockMaterial == BasicAir.AIR || blockMaterial == VanillaMaterials.WATER || blockMaterial == VanillaMaterials.LAVA) {
			isInteractable = false;
		}

		InventorySlot currentSlot = VanillaPlayerUtil.getCurrentSlot(player);
		ItemStack heldItem = currentSlot.getItem();

		if (state == PlayerDiggingMessage.STATE_START_DIGGING) {
			PlayerInteractEvent event = new PlayerInteractEvent(player, block.getPosition(), heldItem, Action.LEFT_CLICK, isInteractable);
			if (Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
				return;
			}

			// Perform interactions
			if (!isInteractable && heldItem == null) {
				// interacting with nothing using fist
				return;
			} else if (heldItem == null) {
				// interacting with block using fist
				blockMaterial.onInteractBy(player, block, Action.LEFT_CLICK, clickedFace);
			} else if (!isInteractable) {
				// interacting with nothing using item
				heldItem.getMaterial().onInteract(player, Action.LEFT_CLICK);
			} else {
				// interacting with block using item
				heldItem.getMaterial().onInteract(player, block, Action.LEFT_CLICK, clickedFace);
				blockMaterial.onInteractBy(player, block, Action.LEFT_CLICK, clickedFace);
			}
			// Interaction with entity
			if (blockMaterial.hasController()) {
				blockMaterial.getController(block).onInteract(player, Action.LEFT_CLICK);
			}

			if (isInteractable) {
				Block neigh = block.translate(clickedFace);
				boolean fire = neigh.getMaterial().equals(VanillaMaterials.FIRE);
				if (fire) {
					// put out fire
					VanillaMaterials.FIRE.onDestroy(neigh);
					GeneralEffects.RANDOM_FIZZ.playGlobal(block.getPosition());
				} else if (vp.isSurvival() && blockMaterial.getHardness() != 0.0f) {
					vp.getDiggingLogic().startDigging(new Point(w, x, y, z));
				} else {
					// insta-break
					breakBlock(blockMaterial, block, vp);
					GeneralEffects.BREAKBLOCK.playGlobal(block.getPosition(), blockMaterial, player);
				}
			}
		} else if (state == PlayerDiggingMessage.STATE_DONE_DIGGING) {
			if (!vp.getDiggingLogic().stopDigging(new Point(w, x, y, z)) || !isInteractable) {
				return;
			}

			if (VanillaPlayerUtil.isSurvival(player)) {
				if (vp.isSurvival()) {
					vp.getSurvivalComponent().addExhaustion(ExhaustionLevel.BREAK_BLOCK.getAmount());
				}

				long diggingTicks = vp.getDiggingLogic().getDiggingTicks();
				int damageDone;
				int totalDamage;

				if (heldItem != null && heldItem.getMaterial() instanceof Tool) {
					currentSlot.addItemData(((Tool) heldItem.getMaterial()).getDurabilityPenalty(heldItem));
				}
				if (heldItem == null) {
					damageDone = ((int) diggingTicks * 1);
				} else {
					damageDone = ((int) diggingTicks * ((VanillaMaterial) heldItem.getMaterial()).getDamage());
				}
				// TODO: Take into account EFFICIENCY enchantment
				// TODO: Digging is slower while under water, on ladders, etc. AQUA_AFFINITY enchantment speeds up underwater digging

				totalDamage = ((int) blockMaterial.getHardness() - damageDone);
				if (totalDamage <= 40) { // Yes, this is a very high allowance - this is because this is only over a single block, and this will spike due to varying latency.
					breakBlock(blockMaterial, block, vp);
				}
				if (block.getMaterial() != VanillaMaterials.AIR) {
					GeneralEffects.BREAKBLOCK.playGlobal(block.getPosition(), blockMaterial, player);
				}
			}
		} else if (state == PlayerDiggingMessage.STATE_SHOOT_ARROW_EAT_FOOD) {
			if (heldItem.getMaterial() instanceof Food) {
				((Food) heldItem.getMaterial()).onEat(player, currentSlot);
			}
		}
	}
}
