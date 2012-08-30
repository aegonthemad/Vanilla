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
package org.spout.vanilla.entity.component.effect;

import org.spout.api.entity.BasicComponent;
import org.spout.api.protocol.Message;

import org.spout.vanilla.entity.VanillaPlayerController;

/**
 * Represents an entity effect that is applied to an entity.
 */
public abstract class EntityEffect extends BasicComponent<VanillaPlayerController> {
	protected int id;
	protected float strength;

	public EntityEffect(VanillaPlayerController effected, int id, float duration, float strength) {
		this.id = id;
		this.strength = strength;
		this.setDelay(duration);
		this.setRunOnce(true);
	}

	/**
	 * Whether or not to send a message to the client when the effect starts.
	 * @return true if has appliance message
	 */
	public abstract boolean hasApplianceMessage();

	/**
	 * Whether or not the send a message to the client when the effects ends.
	 * @return true if has removal message
	 */
	public abstract boolean hasRemovalMessage();

	/**
	 * Gets the message sent to the client when the effect activates.
	 * @return message to send
	 */
	public abstract Message getApplianceMessage();

	/**
	 * Gets the message sent to the client when the effect is removed.
	 * @return message to send
	 */
	public abstract Message getRemovalMessage();

	/**
	 * Gets the id of the effect.
	 * @return id of effect
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the strength of the effect.
	 * @return strength of effect.
	 */
	public float getStrength() {
		return strength;
	}

	/**
	 * Sets the strength of the effect.
	 * @param strength of effect
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}

	@Override
	public void onAttached() {
		getParent().getParent().getSession().send(false, getApplianceMessage());
	}

	@Override
	public boolean canTick() {
		return super.canTick();
	}

	@Override
	public void onTick(float dt) {
		getParent().getParent().getSession().send(false, getRemovalMessage());
	}
}
