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

<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/msg/TimeUpdateMessage.java
public final class TimeUpdateMessage implements Message {
	private final long timeOfDay;
	private final long ageOfWorld;

	public TimeUpdateMessage(long ageOfWorld, long timeOfDay) {
		this.ageOfWorld = ageOfWorld;
		this.timeOfDay = timeOfDay;
	}

	public long getAgeOfWorld() {
        return ageOfWorld;
    }
	
	public long getTimeOfDay() {
        return timeOfDay;
    }
=======
public final class PlayerTimeMessage implements Message {
	private final long age;
	private final long time;

	public PlayerTimeMessage(long age, long time) {
		this.age = age;
		this.time = time;
	}

	public long getAge() {
		return age;
	}

	public long getTime() {
		return time;
	}
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/msg/player/PlayerTimeMessage.java

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/msg/TimeUpdateMessage.java
				.append("timeOfDay", timeOfDay)
				.append("ageOfWorld", ageOfWorld)
=======
				.append("age", age)
				.append("time", time)
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/msg/player/PlayerTimeMessage.java
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
<<<<<<< HEAD:src/main/java/org/spout/vanilla/protocol/msg/TimeUpdateMessage.java
		final TimeUpdateMessage other = (TimeUpdateMessage) obj;
		return this.timeOfDay == other.timeOfDay && this.ageOfWorld == other.ageOfWorld;
=======
		final PlayerTimeMessage other = (PlayerTimeMessage) obj;
		return this.time == other.time;
>>>>>>> 43c4837603f8d11e79b43629e6d211aac83e5e42:src/main/java/org/spout/vanilla/protocol/msg/player/PlayerTimeMessage.java
	}
}
