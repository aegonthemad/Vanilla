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
package org.spout.vanilla.entity;

import org.spout.api.entity.Player;
import org.spout.api.entity.controller.BlockController;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.material.VanillaBlockMaterial;

/**
 * A entity that is always at a fixed position handling Block logic a Block material can't do
 */
public abstract class VanillaBlockController extends BlockController implements VanillaController {
	protected VanillaBlockController(VanillaControllerType type, VanillaBlockMaterial blockMaterial) {
		super(type, blockMaterial);
	}

	@Override
	public void callProtocolEvent(ProtocolEvent event) {
		for (Player player : getParent().getWorld().getNearbyPlayers(getParent(), 160)) {
			player.getNetworkSynchronizer().callProtocolEvent(event);
		}
	}

	@Override
	public VanillaBlockMaterial getMaterial() {
		return (VanillaBlockMaterial) super.getMaterial();
	}

	/**
	 * Plays a block action<br>
	 * See also: VanillaBlockMaterial.playBlockAction
	 * 
	 * @param arg1 for the action
	 * @param arg2 for the action
	 */
	public void playBlockAction(byte arg1, byte arg2) {
		getMaterial().playBlockAction(getBlock(), arg1, arg2);
	}
}
