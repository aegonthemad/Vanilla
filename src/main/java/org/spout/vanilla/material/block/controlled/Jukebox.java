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
package org.spout.vanilla.material.block.controlled;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.item.tool.Axe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Jukebox extends ControlledMaterial implements Fuel, Mineable {
	public final float BURN_TIME = 15.f;

	public Jukebox(String name, int id) {
		super(VanillaControllerTypes.JUKEBOX, name, id);
		this.setHardness(2.0F).setResistance(10.0F);
	}

	@Override
	public void onDestroy(Block block, double dropChance) {
		((org.spout.vanilla.controller.block.Jukebox) block.getController()).stopMusic();
		super.onDestroy(block, dropChance);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type == Action.RIGHT_CLICK) {
			org.spout.vanilla.controller.block.Jukebox controller = (org.spout.vanilla.controller.block.Jukebox) block.getController();
			controller.eject();
			InventorySlot inv = VanillaPlayerUtil.getCurrentSlot(entity);
			if (inv != null && controller.canPlay(inv.getItem())) {
				controller.getInventory().addItem(inv.getItem().clone().setAmount(1));
				controller.update();
				if (VanillaPlayerUtil.isSurvival(entity)) {
					inv.addItemAmount(0, -1);
				}
			}
		} else if (type == Action.RIGHT_CLICK) {
			((org.spout.vanilla.controller.block.Jukebox) block.getController()).stopMusic();
		}
	}

	@Override
	public Instrument getInstrument() {
		return Instrument.BASSGUITAR;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Axe ? (short) 1 : (short) 2;
	}
}
