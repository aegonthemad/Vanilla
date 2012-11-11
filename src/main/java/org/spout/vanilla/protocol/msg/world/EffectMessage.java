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
package org.spout.vanilla.protocol.msg.world;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class EffectMessage implements Message {
	private final int id;
	private final int x, y, z;
	private final int data;
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/msg/PlayEffectMessage.java
	private boolean volumeDecrease;

	public PlayEffectMessage(int id, Block block, int data, boolean volumeDecrease) {
		this(id, block.getX(), block.getY(), block.getZ(), data, volumeDecrease);
	}

	public PlayEffectMessage(int id, int x, int y, int z, int data, boolean volumeDecrease) {
=======
	private final boolean volumeDecrease;

	public EffectMessage(int id, Block block, int data) {
		this(id, block, data, true);
	}

	public EffectMessage(int id, Block block, int data, boolean volumeDecrease) {
		this(id, block.getX(), block.getY(), block.getZ(), data, volumeDecrease);
	}

	public EffectMessage(int id, int x, int y, int z, int data) {
		this(id, x, y, z, data, true);
	}

	public EffectMessage(int id, int x, int y, int z, int data, boolean volumeDecrease) {
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/msg/world/EffectMessage.java
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
		this.volumeDecrease = volumeDecrease;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}
	
    public boolean isVolumeDecrease() {
        return volumeDecrease;
    }

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getData() {
		return data;
	}
	
	public boolean isNoVolumeDecrease() {
        return volumeDecrease;
    }

	public boolean hasVolumeDecrease() {
		return volumeDecrease;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("data", data)
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/msg/PlayEffectMessage.java
				.append("volumeDecrease", volumeDecrease)
=======
				.append("no_volume_decrease", volumeDecrease)
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/msg/world/EffectMessage.java
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
		final EffectMessage other = (EffectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.data, other.data)
				.append(this.volumeDecrease, other.volumeDecrease)
				.isEquals();
	}


}
