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
package org.spout.vanilla.material.block.solid;

import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.SpreadingSolid;

public class Mycelium extends SpreadingSolid implements InitializableMaterial {
	public Mycelium(String name, int id) {
		super(name, id, "model://Vanilla/resources/materials/block/solid/mycelium/mycelium.spm");
		this.setHardness(0.6F).setResistance(0.8F).addMiningType(ToolType.SPADE);
	}

	@Override
	public void initialize() {
		setReplacedMaterial(VanillaMaterials.DIRT);
		getDrops().DEFAULT.clear();
		getDrops().DEFAULT.add(VanillaMaterials.DIRT);
		getDrops().SILK_TOUCH.add(VanillaMaterials.MYCELIUM);
	}

	@Override
	public int getMinimumLightToSpread() {
		return 9;
	}

	@Override
	public boolean canDecayAt(Block block) {
		block = block.translate(BlockFace.TOP);
		return block.getMaterial().getOpacity() > 1 && block.getLight() < 4;
	}

	@Override
	public long getSpreadingTime(Block b) {
		return 120000L + new Random(b.getWorld().getAge()).nextInt(60000) * 5;
	}
}
