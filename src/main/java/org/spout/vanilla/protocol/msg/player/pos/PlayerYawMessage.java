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
package org.spout.vanilla.protocol.msg.player.pos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.math.SinusHelper;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class PlayerYawMessage implements Message {
	private final float yaw, pitch, roll;
	private final boolean onGround;
	private final Vector3 lookingAt;

	public PlayerYawMessage(float yaw, float pitch, boolean onGround) {
		this.roll = 0.0f; // There is no roll
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.lookingAt = SinusHelper.get3DAxis((float) Math.toRadians(yaw), (float) Math.toRadians(pitch));
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public Vector3 getLookingAtVector() {
		return lookingAt;
	}

	public boolean isOnGround() {
		return onGround;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("yaw", yaw)
				.append("pitch", pitch)
				.append("roll", roll)
				.append("onGround", onGround)
				.append("lookingAt", lookingAt)
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
		final PlayerYawMessage other = (PlayerYawMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.yaw, other.yaw)
				.append(this.pitch, other.pitch)
				.append(this.roll, other.roll)
				.append(this.onGround, other.onGround)
				.append(this.lookingAt, other.lookingAt)
				.isEquals();
	}
}
