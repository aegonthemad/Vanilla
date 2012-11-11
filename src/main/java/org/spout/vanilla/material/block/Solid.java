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
package org.spout.vanilla.material.block;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class Solid extends VanillaBlockMaterial {

	public Solid(String name, int id, String model) {
		this((short) 0, name, id, model);
	}

	public Solid(short dataMask, String name, int id, String model) {
		super(dataMask, name, id, model);
		this.setCollision(CollisionStrategy.SOLID).setOpaque();
	}

	public Solid(String name, int id, int data, VanillaBlockMaterial parent, String model) {
		super(name, id, data, parent, model);
		this.setCollision(CollisionStrategy.SOLID).setOpaque();
	}

	@Override
	public boolean isRedstoneConductor() {
		return true;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.ALLOW;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		// Solids only support fire on top
		if (material.equals(VanillaMaterials.FIRE)) {
			return face == BlockFace.TOP;
		}

		// Anything else is supported to all sides
		return true;
	}
}
