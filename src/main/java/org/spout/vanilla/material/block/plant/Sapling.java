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
package org.spout.vanilla.material.block.plant;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Plant;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.solid.Log;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.VanillaPlayerUtil;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;

public class Sapling extends GroundAttachable implements Plant, Fuel, DynamicMaterial {
	public static final Sapling DEFAULT = new Sapling("Sapling");
	public static final Sapling SPRUCE = new Sapling("Spruce Sapling", 1, DEFAULT);
	public static final Sapling BIRCH = new Sapling("Birch Sapling", 2, DEFAULT);
	public static final Sapling JUNGLE = new Sapling("Jungle Sapling", 3, DEFAULT);
	public final float BURN_TIME = 5.f;
	private static final short dataMask = 0x3;

	private Sapling(String name) {
		super(dataMask, name, 6);
		this.setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	private Sapling(String name, int data, Sapling parent) {
		super(name, 6, data, parent);
		this.setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	@Override
	public boolean hasGrowthStages() {
		return true;
	}

	@Override
	public int getNumGrowthStages() {
		return 3;
	}

	@Override
	public int getMinimumLightToGrow() {
		return 8;
	}

	@Override
	public float getFuelTime() {
		return BURN_TIME;
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (super.canAttachTo(material, face)) {
			return material.equals(VanillaMaterials.GRASS, VanillaMaterials.DIRT);
		}
		return false;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != Action.RIGHT_CLICK) {
			return;
		}
		InventorySlot inv = VanillaPlayerUtil.getCurrentSlot(entity);
		ItemStack current = inv.getItem();
		if (current != null && current.getSubMaterial().equals(Dye.BONE_MEAL)) {
			if (VanillaPlayerUtil.isSurvival(entity)) {
				inv.addItemAmount(0, -1);
			}
			this.growTree(block);
		}
	}

	/**
	 * Grows a full-sized tree from the sapling at the block given
	 * @param block to place a tree at
	 */
	public void growTree(Block block) {
		BlockMaterial mat = block.getMaterial();
		if (mat instanceof Sapling) {
			this.growTree(block, (Sapling) mat);
		}
	}

	/**
	 * Grows a full-sized tree from the sapling type given
	 * @param block to grow a tree at
	 * @param type of tree
	 */
	public void growTree(Block block, Sapling type) {
		TreeObject.growTree(type, block, new Random());
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		block.clearDataBits((short) (~dataMask));
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		b.dynamicUpdate(currentTime + 10000);
	}

	@Override
	public void onDynamicUpdate(Block b, Region r, long updateTime, long lastUpdateTime, int data, Object hint) {
		short oldData = b.getData();
		b.setMaterial(Log.DEFAULT);
		b.setData(oldData & dataMask);
		b.setDataBits(Log.aliveMask);
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}
}
