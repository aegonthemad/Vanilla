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

import org.spout.api.material.source.DataSource;

import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.block.Solid;

public class Sandstone extends Solid {
	public static final Sandstone SANDSTONE = new Sandstone("Sandstone","model://Vanilla/resources/materials/block/solid/sandstone/sandstone.spm");
	public static final Sandstone DECORATIVE = new Sandstone("Decorative Sandstone", SandstoneType.DECORATIVE, SANDSTONE,"model://Vanilla/resources/materials/block/solid/cheseledsandstone/cheseledsandstone.spm");
	public static final Sandstone SMOOTH = new Sandstone("Smooth Sandstone", SandstoneType.SMOOTH, SANDSTONE,"model://Vanilla/resources/materials/block/solid/smoothsandstone/smoothsandstone.spm");
	private final SandstoneType type;

	private Sandstone(String name, String model) {
		super((short) 0x0003, name, 24, model);
		this.type = SandstoneType.SANDSTONE;
		this.setHardness(0.8F).setResistance(1.3F).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	private Sandstone(String name, SandstoneType type, Sandstone parent, String model) {
		super(name, 24, type.getData(), parent, model);
		this.type = type;
		this.setHardness(0.8F).setResistance(1.3F).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	public SandstoneType getType() {
		return type;
	}

	@Override
	public Sandstone getParentMaterial() {
		return (Sandstone) super.getParentMaterial();
	}

	public static enum SandstoneType implements DataSource {
		SANDSTONE(0),
		DECORATIVE(1),
		SMOOTH(2),;
		private final short data;

		private SandstoneType(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}
	}
}
