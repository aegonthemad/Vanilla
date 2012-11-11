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
package org.spout.vanilla.material.item;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.GenericMaterialSource;
import org.spout.api.material.source.MaterialSource;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaBlockMaterial;

/**
 * A simplistic class which redirects placement requests to another (official) block material<br>
 * Can be used to store multi-block creations
 */
public class BlockItem extends VanillaItemMaterial implements Placeable {
	private final BlockMaterial place;

	public BlockItem(String name, int id, int data, Material parent, BlockMaterial place) {
		super(name, id, data, parent);
		this.place = place;
	}

	public BlockItem(String name, int id, BlockMaterial place) {
		super(name, id);
		this.place = place;
	}

	public BlockItem(short dataMask, String name, int id, BlockMaterial place) {
		super(dataMask, name, id);
		this.place = place;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		return place.canPlace(block, place.getData(), against, clickedPos, isClickedBlock);
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		return place.onPlacement(block, place.getData(), against, clickedPos, isClickedBlock, cause);
	}

	@Override
	public final boolean canPlace(Block block, short data) {
		return canPlace(block, data, BlockFace.BOTTOM, Vector3.UNIT_Y, false);
	}

	@Override
	public final boolean onPlacement(Block block, short data, Cause<?> cause) {
		return onPlacement(block, data, BlockFace.BOTTOM, Vector3.UNIT_Y, false, cause);
	}

	/**
	 * Gets the block material this block item places
	 * @return the Block material
	 */
	public BlockMaterial getPlacedMaterial() {
		return place;
	}
}
