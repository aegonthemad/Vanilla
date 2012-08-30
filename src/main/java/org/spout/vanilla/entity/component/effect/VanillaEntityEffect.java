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

import org.spout.api.protocol.Message;

import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.protocol.msg.entity.EntityEffectMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRemoveEffectMessage;

public abstract class VanillaEntityEffect extends EntityEffect {
	public VanillaEntityEffect(VanillaPlayerController effected, int id, float duration, int strength) {
		super(effected, id, duration, strength);
	}

	@Override
	public boolean hasApplianceMessage() {
		return true;
	}

	@Override
	public boolean hasRemovalMessage() {
		return true;
	}

	@Override
	public Message getApplianceMessage() {
		return new EntityEffectMessage(getParent().getParent().getId(), (byte) id, (byte) strength, (byte) (getDelay() * 20));
	}

	@Override
	public Message getRemovalMessage() {
		return new EntityRemoveEffectMessage(getParent().getParent().getId(), (byte) id);
	}
}
