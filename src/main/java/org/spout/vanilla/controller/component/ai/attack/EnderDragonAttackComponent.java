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
package org.spout.vanilla.controller.component.ai.attack;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.creature.hostile.EnderDragon;

/**
 * The EnderDragon's attack component which involves randomly flying around, setting up for a
 * potential "hit and run" against a controller.
 */
public class EnderDragonAttackComponent extends LogicRunnable<EnderDragon> {
	public EnderDragonAttackComponent(EnderDragon parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public boolean shouldRun(float dt) {
		return false; //TODO Should this extend AttackLogic and call super?
	}

	@Override
	public void run() {
		//TODO Watch the player and (most of the time) attack whenever their back is turned!
	}
}
