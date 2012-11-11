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
package org.spout.vanilla.material.block.component;

import org.spout.api.component.components.BlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.cause.EntityCause;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.protocol.event.ProtocolEvent;

import org.spout.vanilla.component.misc.HeadComponent;
import org.spout.vanilla.component.substance.material.Sign;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.event.block.SignUpdateEvent;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;

public abstract class SignBase extends AbstractAttachable implements InitializableMaterial, VanillaComplexMaterial {
	public SignBase(String name, int id) {
		super(name, id, (String) null);
		this.setAttachable(BlockFaces.NESWB).setHardness(1.0F).setResistance(1.6F).setOpacity((byte) 1);
	}

	@Override
	public void initialize() {
		getDrops().add(VanillaMaterials.SIGN);
	}

	@Override
	public void handlePlacement(Block block, short data, BlockFace attachedFace, Cause<?> cause) {
		this.setAttachedFace(block, attachedFace, cause);
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		if (attachedFace == BlockFace.BOTTOM) {
			short data = 0;
			if (cause instanceof EntityCause) {
				Entity entity = ((EntityCause) cause).getSource();
				float yaw = entity.getTransform().getYaw();
				float rotation = (yaw + 180F) * 16F / 360F;
				data = (short) (rotation + 0.5F);
				data &= 15;
			}
			block.setMaterial(VanillaMaterials.SIGN_POST, data, cause);
		} else {
			// get the data for this face
			short data = (short) (BlockFaces.WESN.indexOf(attachedFace, 0) + 2);
			block.setMaterial(VanillaMaterials.WALL_SIGN, data, cause);
		}
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.WESN.get(data - 2);
	}

	@Override
	public abstract boolean canSupport(BlockMaterial material, BlockFace face);

	@Override
	public BlockComponent createBlockComponent() {
		return new Sign();
	}

	@Override
	public ProtocolEvent getUpdate(World world, int x, int y, int z) {
		Sign sign = (Sign) world.getBlockComponent(x, y, z);
		return new SignUpdateEvent(sign, sign.getText(), null);
	}
}
