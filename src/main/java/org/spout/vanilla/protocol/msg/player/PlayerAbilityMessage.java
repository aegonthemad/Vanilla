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
package org.spout.vanilla.protocol.msg.player;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class PlayerAbilityMessage implements Message {
	private final boolean godMode, isFlying, canFly, creativeMode;
	private final byte flyingSpeed, walkingSpeed;

	public PlayerAbilityMessage(boolean godMode, boolean isFlying, boolean canFly, boolean creativeMode, byte flyingSpeed, byte walkingSpeed) {
		this.godMode = godMode;
		this.isFlying = isFlying;
		this.canFly = canFly;
		this.creativeMode = creativeMode;
		this.flyingSpeed = flyingSpeed;
		this.walkingSpeed = walkingSpeed;
	}

	public boolean isGodMode() {
		return godMode;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public boolean canFly() {
		return canFly;
	}

	public boolean isCreativeMode() {
		return creativeMode;
	}

	public byte getFlyingSpeed() {
		return flyingSpeed;
	}

	public byte getWalkingSpeed() {
		return walkingSpeed;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("godMode", godMode)
				.append("isFlying", isFlying)
				.append("canFly", canFly)
				.append("creativeMode", creativeMode)
				.append("flyingSpeed", flyingSpeed)
				.append("walkingSpeed", walkingSpeed)
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
		final PlayerAbilityMessage other = (PlayerAbilityMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.godMode, other.godMode)
				.append(this.isFlying, other.isFlying)
				.append(this.canFly, other.canFly)
				.append(this.creativeMode, other.creativeMode)
				.append(this.flyingSpeed, other.flyingSpeed)
				.append(this.walkingSpeed, other.walkingSpeed)
				.isEquals();
	}
}
