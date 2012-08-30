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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class PlayerListMessage implements Message {
	private final String playerName;
	private final boolean playerOnline;
	private final short ping;

	public PlayerListMessage(String playerName, boolean playerOnline, short ping) {
		this.playerName = playerName;
		this.playerOnline = playerOnline;
		this.ping = ping;
	}

	public String getPlayerName() {
		return playerName;
	}

	public boolean playerIsOnline() {
		return playerOnline;
	}
	
	public boolean isPlayerOnline() {
		return playerOnline;
	}

	public short getPing() {
		return ping;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("playerName", playerName)
				.append("playerOnline", playerOnline)
				.append("ping", ping)
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
		final PlayerListMessage other = (PlayerListMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.playerName, other.playerName)
				.append(this.playerOnline, other.playerOnline)
				.append(this.ping, other.ping)
				.isEquals();
	}
}
