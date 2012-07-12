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
package org.spout.vanilla.material.block.misc;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class BedBlock extends VanillaBlockMaterial implements Mineable, InitializableMaterial {
	public BedBlock(String name, int id) {
		super(name, id);
		this.setHardness(0.2F).setResistance(0.3F).setTransparent();
	}

	@Override
	public void initialize() {
		this.setDropMaterial(VanillaMaterials.BED);
	}

	@Override
	public void onDestroyBlock(Block block) {
		Block head = getCorrectHalf(block, true);
		Block foot = getCorrectHalf(block, false);
		head.setMaterial(VanillaMaterials.AIR);
		foot.setMaterial(VanillaMaterials.AIR);
	}

	/**
	 * Sets whether or not a bed is occupied by a player
	 * @param bedBlock to get it of
	 * @return True if occupied
	 */
	public void setOccupied(Block bedBlock, boolean occupied) {
		bedBlock = getCorrectHalf(bedBlock, false);
		bedBlock.setDataBits(0x4, occupied);
		//set to the same data for the head, but set the head flag
		getCorrectHalf(bedBlock, true).setData(bedBlock.getData() | 0x8);
	}

	/**
	 * Gets whether or not a bed block is occupied by a player
	 * @param bedBlock to get it of
	 * @return True if occupied
	 */
	public boolean isOccupied(Block bedBlock) {
		return bedBlock.isDataBitSet(0x4);
	}

	/**
	 * Gets the facing state of a single bed block
	 * @param bedBlock to get it of
	 * @return the face
	 */
	public BlockFace getFacing(Block bedBlock) {
		return BlockFaces.WNES.get(bedBlock.getData() & 0x3);
	}

	/**
	 * Sets the facing state of a single bed block<br>
	 * Note that this does not affect the misc half
	 * @param bedBlock to set it of
	 * @param facing to set to
	 * @return the face
	 */
	public void setFacing(Block bedBlock, BlockFace facing) {
		bedBlock.setDataField(0x3, BlockFaces.WNES.indexOf(facing, 0));
	}

	/**
	 * Creates a bed using the parameters specified
	 * 
	 * @param footBlock of the bed
	 * @param facing of the bed
	 */
	public void create(Block footBlock, BlockFace facing) {
		Block headBlock = footBlock.translate(facing);
		footBlock.setMaterial(this, 0x0);
		headBlock.setMaterial(this, 0x8);
		setFacing(footBlock, facing);
		setFacing(headBlock, facing);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (against == BlockFace.BOTTOM && super.canPlace(block, data, against, isClickedBlock)) {
			Block below = block.translate(BlockFace.BOTTOM);
			BlockMaterial material = below.getMaterial();
			if (material instanceof VanillaBlockMaterial) {
				return ((VanillaBlockMaterial) material).canSupport(this, BlockFace.TOP);
			}
		}
		return false;
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace face, boolean isClicked) {
		if (face == BlockFace.BOTTOM) {
			BlockFace facing = VanillaPlayerUtil.getFacing(block.getSource());
			Block head = block.translate(facing);
			// Check if the head block can be placed
			if (this.canPlace(head, data, face, false)) {
				create(block, facing);
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the top or face door block when either of the blocks is given
	 * @param bedBlock the top or bottom bed block
	 * @param head whether to get the top block, if false, gets the bottom block
	 * @return the requested bed half block
	 */
	private Block getCorrectHalf(Block bedBlock, boolean head) {
		BlockFace facing = getFacing(bedBlock);
		if (bedBlock.isDataBitSet(0x8)) {
			if (!head) {
				bedBlock = bedBlock.translate(facing.getOpposite());
			}
		} else {
			if (head) {
				bedBlock = bedBlock.translate(facing);
			}
		}
		if (!bedBlock.getMaterial().equals(this)) {
			//create default bed block to 'fix' things up
			bedBlock.setMaterial(this, head ? 0x8 : 0x0);
			//find out what facing makes most sense
			for (BlockFace face : BlockFaces.NESW) {
				if (bedBlock.translate(face).getMaterial().equals(this)) {
					if (head) {
						setFacing(bedBlock, face.getOpposite());
					} else {
						setFacing(bedBlock, face);
					}
					break;
				}
			}
		}
		return bedBlock;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}


}
