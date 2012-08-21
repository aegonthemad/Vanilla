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
package org.spout.vanilla.controller.object.moving;

import org.spout.api.tickable.LogicPriority;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.component.physics.DetectXPCollectorComponent;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.data.VanillaData;

public class XPOrb extends Substance {
	private short experience;
	private long timeDispersed;

	public XPOrb(short experience) {
		super(VanillaControllerTypes.XP_ORB);
		this.experience = experience;
	}

	@Override
	public void onAttached() {
		super.onAttached();
		experience = data().get(VanillaData.EXPERIENCE_AMOUNT);
		timeDispersed = data().get(VanillaData.TIME_DISPERSED);
		registerProcess(new DetectXPCollectorComponent(this, LogicPriority.NORMAL));
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put(VanillaData.EXPERIENCE_AMOUNT, experience);
		data().put(VanillaData.TIME_DISPERSED, timeDispersed);
	}

	public short getExperience() {
		return experience;
	}

	public void setExperience(short experience) {
		this.experience = experience;
	}

	public void setTimeDispersed(long timeDispersed) {
		this.timeDispersed = timeDispersed;
	}

	public long getTimeDispersed() {
		return timeDispersed;
	}
}
