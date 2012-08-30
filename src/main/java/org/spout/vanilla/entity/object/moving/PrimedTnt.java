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
package org.spout.vanilla.entity.object.moving;

import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.object.Substance;
import org.spout.vanilla.util.explosion.ExplosionModels;

public class PrimedTnt extends Substance {
	private float timeToExplode = 4.f;
	private float radius = 4.f;

	public PrimedTnt() {
		super(VanillaControllerTypes.PRIMED_TNT);
	}

	public void setExplosionRadius(float radius) {
		this.radius = radius;
	}

	public float getExplosionRadius() {
		return this.radius;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		// gravity
		this.setVelocity(this.getVelocity().subtract(0, 0.04, 0));
		this.move();
		// slow-down
		this.setVelocity(this.getVelocity().multiply(0.98));

		//TODO: proper entity on ground function
		if (getParent().getWorld().getBlock(getParent().getPosition(), getParent()).translate(BlockFace.BOTTOM).getMaterial().isSolid()) {
			this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
		}

		timeToExplode -= dt;
		if (timeToExplode <= 0.0f) {
			//TODO: Event? We don't have the blocks, as that is internally handled...
			ExplosionModels.SPHERICAL.execute(this.getParent().getPosition(), this.radius);
			this.getParent().kill();
			return;
		}
		super.onTick(dt);
	}
}
