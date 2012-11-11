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
package org.spout.vanilla.world.generator.structure.temple;

import java.util.Collections;
import java.util.List;

import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.material.block.solid.Sandstone;
import org.spout.vanilla.material.block.solid.Wool;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.BlockMaterialLayout;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.ComponentLayoutPainterPart;
import org.spout.vanilla.world.generator.structure.ComponentPlanePart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class DesertTemple extends StructureComponent {
	private static final LootChestObject LOOT_CHEST = new LootChestObject();
	private static final BlockMaterialLayout CENTER_CROSS;
	private static final BlockMaterialLayout TOWER;
	private static final BlockMaterialLayout DOOR;

	static {
		LOOT_CHEST.addMaterial(VanillaMaterials.IRON_BARS, 0.1, 1, 3); //TODO Investigate how the materials are distributed
		CENTER_CROSS = new BlockMaterialLayout("...o...\n...o...\n..o.o..\noo.b.oo\n..o.o..\n...o...\n...o...");
		CENTER_CROSS.setBlockMaterial('o', Wool.ORANGE_WOOL);
		CENTER_CROSS.setBlockMaterial('b', Wool.BLUE_WOOL);
		TOWER = new BlockMaterialLayout("-oo-o--\n-o=o=oo\n-oo-o--");
		TOWER.setBlockMaterial('-', Sandstone.SMOOTH);
		TOWER.setBlockMaterial('o', Wool.ORANGE_WOOL);
		TOWER.setBlockMaterial('=', Sandstone.DECORATIVE);
		DOOR = new BlockMaterialLayout(".--\n-o-\n-=-\n-o-\n.--");
		DOOR.setBlockMaterial('-', Sandstone.SMOOTH);
		DOOR.setBlockMaterial('o', Wool.ORANGE_WOOL);
		DOOR.setBlockMaterial('=', Sandstone.DECORATIVE);
	}

	public DesertTemple(Structure parent) {
		super(parent);
	}

	@Override
	public void randomize() {
	}

	@Override
	public void place() {
		// Building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final ComponentPlanePart plane = new ComponentPlanePart(this);
		final ComponentLayoutPainterPart painter = new ComponentLayoutPainterPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		plane.setPicker(picker);
		// Foundations
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(0, -4, 0, 20, 0, 20);
		box.fill(false);
		// Pyramid
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		plane.setMinMax(0, 0, 0, 20, 0, 20);
		for (byte yy = 1; yy < 10; yy++) {
			plane.offsetMinMax(1, 1, 1, -1, 1, -1);
			plane.fill(false);
		}
		// Fill the land under
		for (byte xx = 0; xx < 21; xx++) {
			for (byte zz = 0; zz < 21; zz++) {
				fillDownwards(xx, -5, zz, (short) 50, VanillaMaterials.SANDSTONE);
			}
		}
		// Tower bodies
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(0, 0, 0, 4, 9, 4);
		box.fill(false);
		box.offsetMinMax(16, 0, 0, 16, 0, 0);
		box.fill(false);
		picker.setInnerMaterial(VanillaMaterials.SANDSTONE);
		box.setMinMax(1, 10, 1, 3, 10, 3);
		box.fill(false);
		box.offsetMinMax(16, 0, 0, 16, 0, 0);
		box.fill(false);
		setBlockMaterial(2, 10, 0, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(2, 10, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 3);
		setBlockMaterial(0, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(4, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 1);
		setBlockMaterial(18, 10, 0, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(18, 10, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 3);
		setBlockMaterial(16, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(20, 10, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 1);
		// Entry way
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(8, 0, 0, 12, 4, 4);
		box.fill(false);
		picker.setOuterMaterial(VanillaMaterials.AIR);
		box.offsetMinMax(1, 1, 0, -1, -1, 0);
		box.fill(false);
		setBlockMaterial(9, 1, 1, Sandstone.SMOOTH);
		setBlockMaterial(9, 2, 1, Sandstone.SMOOTH);
		setBlockMaterial(9, 3, 1, Sandstone.SMOOTH);
		setBlockMaterial(10, 3, 1, Sandstone.SMOOTH);
		setBlockMaterial(11, 3, 1, Sandstone.SMOOTH);
		setBlockMaterial(11, 2, 1, Sandstone.SMOOTH);
		setBlockMaterial(11, 1, 1, Sandstone.SMOOTH);
		// Passage ways to the towers
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(4, 1, 1, 8, 3, 3);
		box.fill(false);
		picker.setOuterMaterial(VanillaMaterials.AIR);
		box.offsetMinMax(0, 0, 1, 0, -1, -1);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.AIR);
		box.setMinMax(12, 1, 1, 16, 3, 3);
		box.fill(false);
		picker.setOuterMaterial(VanillaMaterials.AIR);
		box.offsetMinMax(0, 0, 1, 0, -1, -1);
		box.fill(false);
		// Second floor
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(5, 4, 5, 15, 4, 15);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.offsetMinMax(4, 0, 4, -4, 0, -4);
		box.fill(false);
		// Columns on the first floor
		picker.setOuterInnerMaterials(Sandstone.SMOOTH, Sandstone.SMOOTH);
		box.setMinMax(8, 1, 8, 8, 3, 8);
		box.fill(false);
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill(false);
		box.offsetMinMax(-4, 0, 4, -4, 0, 4);
		box.fill(false);
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill(false);
		// Side passage ways to the pyramid on the second floor
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(1, 1, 5, 4, 4, 11);
		box.fill(false);
		box.offsetMinMax(15, 0, 0, 15, 0, 0);
		box.fill(false);
		// The door ways for the passages mentioned above
		box.setMinMax(6, 7, 9, 6, 7, 11);
		box.fill(false);
		box.offsetMinMax(8, 0, 0, 8, 0, 0);
		box.fill(false);
		picker.setOuterInnerMaterials(Sandstone.SMOOTH, Sandstone.SMOOTH);
		box.setMinMax(5, 5, 9, 5, 7, 11);
		box.fill(false);
		box.offsetMinMax(10, 0, 0, 10, 0, 0);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(5, 5, 10, 6, 6, 10);
		box.fill(false);
		box.offsetMinMax(9, 0, 0, 9, 0, 0);
		box.fill(false);
		// Side entry ways to the towers on the second floor
		box.setMinMax(2, 4, 4, 2, 6, 4);
		box.fill(false);
		box.offsetMinMax(16, 0, 0, 16, 0, 0);
		box.fill(false);
		// The staircases for the towers
		setBlockMaterial(2, 4, 5, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(2, 3, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(18, 4, 5, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		setBlockMaterial(18, 3, 4, VanillaMaterials.STAIRS_SANDSTONE, (short) 2);
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(1, 1, 3, 2, 2, 3);
		box.fill(false);
		box.offsetMinMax(17, 0, 0, 17, 0, 0);
		box.fill(false);
		setBlockMaterial(1, 1, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(19, 1, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		setBlockMaterial(1, 2, 2, Slab.SANDSTONE);
		setBlockMaterial(19, 2, 2, Slab.SANDSTONE);
		setBlockMaterial(2, 1, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 1);
		setBlockMaterial(18, 1, 2, VanillaMaterials.STAIRS_SANDSTONE, (short) 0);
		// Setup the spaces for the rows of columns inside
		box.setMinMax(4, 3, 5, 4, 3, 17);
		box.fill(false);
		box.offsetMinMax(12, 0, 0, 12, 0, 0);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(3, 1, 5, 4, 2, 16);
		box.fill(false);
		box.offsetMinMax(13, 0, 0, 13, 0, 0);
		box.fill(false);
		// Now place the columns
		for (int zz = 5; zz < 18; zz += 2) {
			setBlockMaterial(4, 1, zz, Sandstone.SMOOTH);
			setBlockMaterial(4, 2, zz, Sandstone.DECORATIVE);
			setBlockMaterial(16, 1, zz, Sandstone.SMOOTH);
			setBlockMaterial(16, 2, zz, Sandstone.DECORATIVE);
		}
		// Apply the center cross above the loot pit, in the center of the pyramid
		painter.setLayout(CENTER_CROSS);
		painter.setPosition(7, 0, 7);
		painter.fill(false);
		// Apply the glyphs on the towers
		painter.setRotation(new Quaternion(90, 1, 0, 0));
		painter.setLayout(TOWER);
		painter.setPosition(1, 8, 0);
		painter.fill(false);
		painter.offsetPosition(16, 0, 0);
		painter.fill(false);
		// Apply the glyph on the main door
		painter.setLayout(DOOR);
		painter.setPosition(8, 6, 0);
		painter.fill(false);
		// Dig the loot pit
		picker.setOuterInnerMaterials(Sandstone.SMOOTH, Sandstone.SMOOTH);
		box.setMinMax(8, -14, 8, 12, -11, 12);
		box.fill(false);
		picker.setOuterInnerMaterials(Sandstone.DECORATIVE, Sandstone.DECORATIVE);
		box.offsetMinMax(0, 4, 0, 0, 1, 0);
		box.fill(false);
		picker.setOuterInnerMaterials(Sandstone.SMOOTH, Sandstone.SMOOTH);
		box.offsetMinMax(0, 1, 0, 0, 1, 0);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.SANDSTONE, VanillaMaterials.SANDSTONE);
		box.setMinMax(8, -8, 8, 12, -1, 12);
		box.fill(false);
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.offsetMinMax(1, -3, 1, -1, 0, -1);
		box.fill(false);
		// It's a trap! (TNT bellow the floor and stone pressure plate in the middle)
		setBlockMaterial(10, -11, 10, VanillaMaterials.STONE_PRESSURE_PLATE);
		picker.setOuterInnerMaterials(VanillaMaterials.TNT, VanillaMaterials.TNT);
		box.setMinMax(9, -13, 9, 11, -13, 11);
		box.fill(false);
		// Setup the chest spaces
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(8, -11, 10, 8, -10, 10);
		box.fill(false);
		setBlockMaterial(7, -10, 10, Sandstone.DECORATIVE);
		setBlockMaterial(7, -11, 10, Sandstone.SMOOTH);
		box.offsetMinMax(4, 0, 0, 4, 0, 0);
		box.fill(false);
		setBlockMaterial(13, -10, 10, Sandstone.DECORATIVE);
		setBlockMaterial(13, -11, 10, Sandstone.SMOOTH);
		box.offsetMinMax(-2, 0, -2, -2, 0, -2);
		box.fill(false);
		setBlockMaterial(10, -10, 7, Sandstone.DECORATIVE);
		setBlockMaterial(10, -11, 7, Sandstone.SMOOTH);
		box.offsetMinMax(0, 0, 4, 0, 0, 4);
		box.fill(false);
		setBlockMaterial(10, -10, 13, Sandstone.DECORATIVE);
		setBlockMaterial(10, -11, 13, Sandstone.SMOOTH);
		// Place the loot chests
		// TODO: Fix loot chest, and fix loot items for temples and mineshafts
		for (BlockFace face : BlockFaces.NSEW) {
			final Vector3 chestPos = face.getOffset().multiply(2).add(10, -11, 10);
			LOOT_CHEST.setRotation(face.getDirection());
			placeObject(chestPos.getFloorX(), chestPos.getFloorY(), chestPos.getFloorZ(), LOOT_CHEST);
		}
	}

	@Override
	public boolean canPlace() {
		return true;
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		return Collections.emptyList();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(new Vector3(-10, -14, -10), new Vector3(10, 10, 10));
	}
}
