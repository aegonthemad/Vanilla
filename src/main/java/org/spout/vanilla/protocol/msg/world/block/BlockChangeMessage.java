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
package org.spout.vanilla.protocol.msg.world.block;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.material.VanillaMaterials;

public final class BlockChangeMessage implements Message {
	private final int x, y, z, metadata;
	private final short type;

	public BlockChangeMessage(Block block) {
		this(block.getX(), block.getY(), block.getZ(), VanillaMaterials.getMinecraftId(block.getMaterial()), block.getData());
	}

	public BlockChangeMessage(int x, int y, int z, short type, int metadata) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.metadata = metadata;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public short getType() {
		return type;
	}

	public int getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("type", type)
				.append("metadata", metadata)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BlockChangeMessage other = (BlockChangeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.type, other.type)
				.append(this.metadata, other.metadata)
				.isEquals();
	}
}
