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

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.drops.SwitchDrops;
import org.spout.vanilla.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.component.SignBase;

public class Leaves extends Solid implements Burnable, InitializableMaterial {
	public static final Leaves DEFAULT = new Leaves("Leaves", "model://Vanilla/resources/materials/block/solid/oakleaves/oakleaves.spm");
	public static final Leaves SPRUCE = new Leaves("Spruce Leaves", 1, DEFAULT, "model://Vanilla/resources/materials/block/solid/spruceleaves/spruceleaves.spm");
	public static final Leaves BIRCH = new Leaves("Birch Leaves", 2, DEFAULT, "model://Vanilla/resources/materials/block/solid/birchleaves/birchleaves.spm");
	public static final Leaves JUNGLE = new Leaves("Jungle Leaves", 3, DEFAULT, "model://Vanilla/resources/materials/block/solid/jungleleaves/jungleleaves.spm");

	private Leaves(String name, String model) {
		super((short) 0x0003, name, 18, model);
		this.setHardness(0.2F).setResistance(0.3F).setTransparent();
	}

	private Leaves(String name, int data, Leaves parent, String model) {
		super(name, 18, data, parent, model);
		this.setHardness(0.2F).setResistance(0.3F).setTransparent();
	}

	@Override
	public void initialize() {
		SwitchDrops drops = getDrops().DEFAULT.clear().addSwitch(ToolTypeFlags.SHEARS);
		drops.TRUE.add(this);
		drops.FALSE.add(VanillaMaterials.SAPLING).setChance(0.05);
		drops.FALSE.add(VanillaMaterials.RED_APPLE).setChance(0.005);
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		if (mat.isMaterial(VanillaMaterials.FIRE, VanillaMaterials.SNOW, VanillaMaterials.VINES) || mat instanceof SignBase) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getBurnPower() {
		return 30;
	}

	@Override
	public int getCombustChance() {
		return 60;
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	// TODO: Decay
}
