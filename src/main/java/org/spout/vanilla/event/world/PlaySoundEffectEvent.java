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
package org.spout.vanilla.event.world;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.event.ProtocolEvent;

import org.spout.vanilla.data.effect.SoundEffect;

public class PlaySoundEffectEvent extends Event implements ProtocolEvent {
	private static HandlerList handlers = new HandlerList();
	private Point position;
	private SoundEffect sound;
	private float pitch, volume;

	public PlaySoundEffectEvent(Point position, SoundEffect sound, float volume, float pitch) {
		this.position = position;
		this.sound = sound;
		this.pitch = pitch;
		this.volume = volume;
	}

	/**
	 * Gets the Position where the Sound should be played
	 * @return position of the Sound
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * Gets the Sound being played
	 * @return Sound to play
	 */
	public SoundEffect getSound() {
		return this.sound;
	}

	/**
	 * Gets the Pitch to play the Sound at
	 * @return Sound pitch
	 */
	public float getPitch() {
		return this.pitch;
	}

	/**
	 * Gets the Volume to play the Sound at
	 * @return Sound volume
	 */
	public float getVolume() {
		return this.volume;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
