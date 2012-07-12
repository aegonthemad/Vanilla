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
package org.spout.vanilla.world.generator.normal.biome.basic;

import java.util.Random;

import org.spout.vanilla.world.generator.normal.biome.GrassyBiome;
import org.spout.vanilla.world.generator.normal.decorator.FlowerDecorator;
import org.spout.vanilla.world.generator.normal.decorator.MushroomDecorator;
import org.spout.vanilla.world.generator.normal.decorator.OreDecorator;
import org.spout.vanilla.world.generator.normal.decorator.PumpkinDecorator;
import org.spout.vanilla.world.generator.normal.decorator.SandAndClayDecorator;
import org.spout.vanilla.world.generator.normal.decorator.SugarCaneDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator;
import org.spout.vanilla.world.generator.normal.object.tree.PineTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.SpruceTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;

public class TaigaBiome extends GrassyBiome {
	public TaigaBiome(int biomeId) {
		super(biomeId, new OreDecorator(), new SandAndClayDecorator(), new TreeDecorator(new TaigaTreeWGOFactory()),
				new FlowerDecorator(), new TallGrassDecorator(new NormalTallGrassFactory()), new MushroomDecorator(),
				new SugarCaneDecorator(), new PumpkinDecorator());
		setMinMax((byte) 67, (byte) 71);
	}

	@Override
	public String getName() {
		return "Taiga";
	}

	private static class TaigaTreeWGOFactory extends NormalTreeWGOFactory {
		@Override
		public byte amount(Random random) {
			return (byte) (10 + super.amount(random));
		}

		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(3) == 0) {
				return new SpruceTreeObject(random);
			}
			return new PineTreeObject(random);
		}
	}
}
