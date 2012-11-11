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
package org.spout.vanilla.world.generator.normal.biome.grassy;

import java.util.Random;

import org.spout.vanilla.material.block.plant.TallGrass;
import org.spout.vanilla.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.world.generator.normal.decorator.SugarCaneDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator.TallGrassFactory;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.world.generator.normal.decorator.VineDecorator;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public class JungleBiome extends GrassyBiome {
	public JungleBiome(int biomeId) {
		super(biomeId, new SandAndClayDecorator(), new TreeDecorator(new JungleTreeWGOFactory()),
				new FlowerDecorator((byte) 4), new TallGrassDecorator(new JungleTallGrassFactory(), (byte) 15),
				new MushroomDecorator(), new SugarCaneDecorator(), new PumpkinDecorator(), new VineDecorator());
		setMinMax(51, 90);
	}

	@Override
	public String getName() {
		return "Jungle";
	}

	private static class JungleTreeWGOFactory extends NormalTreeWGOFactory {
		@Override
		public byte amount(Random random) {
			return (byte) (50 + super.amount(random));
		}

		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(10) == 0) {
				return VanillaObjects.BIG_OAK_TREE;
			}
			if (random.nextInt(2) == 0) {
				return VanillaObjects.JUNGLE_SHRUB;
			}
			if (random.nextInt(3) == 0) {
				return VanillaObjects.HUGE_JUNGLE_TREE;
			}
			return VanillaObjects.SMALL_JUNGLE_TREE;
		}
	}

	private static class JungleTallGrassFactory implements TallGrassFactory {
		@Override
		public TallGrass make(Random random) {
			if (random.nextInt(4) == 0) {
				return TallGrass.FERN;
			}
			return TallGrass.TALL_GRASS;
		}
	}
}
