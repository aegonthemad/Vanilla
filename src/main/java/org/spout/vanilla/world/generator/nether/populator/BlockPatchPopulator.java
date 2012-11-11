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
package org.spout.vanilla.world.generator.nether.populator;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.nether.NetherGenerator;

public class BlockPatchPopulator implements GeneratorPopulator {
	private final Perlin elevation = new Perlin();
	private final Perlin shapeBase = new Perlin();
	private final Turbulence shape = new Turbulence();
	private final BlockMaterial material;

	public BlockPatchPopulator(BlockMaterial material) {
		this.material = material;

		elevation.setFrequency(0.01);
		elevation.setLacunarity(1);
		elevation.setNoiseQuality(NoiseQuality.STANDARD);
		elevation.setPersistence(0.7);
		elevation.setOctaveCount(1);

		shapeBase.setFrequency(0.03);
		shapeBase.setNoiseQuality(NoiseQuality.STANDARD);
		shapeBase.setOctaveCount(1);

		shape.SetSourceModule(0, shapeBase);
		shape.setFrequency(0.03);
		shape.setPower(8);
		shape.setRoughness(2);
	}

	@Override
	public void populate(CuboidShortBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		seed = WorldGeneratorUtils.getSeed(seed, 7, 7, 7, material.getId());
		final Random random = WorldGeneratorUtils.getRandom(seed, x, y, z, 89545);
		elevation.setSeed((int) (seed * 101));
		shapeBase.setSeed((int) (seed * 313));
		shape.setSeed((int) (seed * 661));
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = MathHelper.clamp(size.getFloorY(), 0, NetherGenerator.HEIGHT);
		final int sizeZ = size.getFloorZ();
		final int scale = sizeY / 2;
		final double[][] displacement = WorldGeneratorUtils.fastNoise(elevation, sizeX, sizeZ, 4, x, 0, z);
		final double[][] values = WorldGeneratorUtils.fastNoise(shape, sizeX, sizeZ, 4, x, 0, z);
		for (byte xx = 0; xx < sizeX; xx++) {
			for (byte zz = 0; zz < sizeZ; zz++) {
				if (values[xx][zz] > 0.65) {
					final int yDisplacement = (int) Math.ceil(displacement[xx][zz] * scale + scale);
					if (yDisplacement < 0 || yDisplacement >= sizeY) {
						continue;
					}
					int yy = getHighestWorkableBlock(blockData, x + xx, yDisplacement + y, z + zz);
					if (yy == -1 || blockData.get(x + xx, yy + 1, z + zz) != VanillaMaterials.AIR.getId()) {
						continue;
					}
					final int depth = random.nextInt(3) + 3;
					for (int yyy = 0; yyy < depth; yyy++) {
						if (blockData.get(x + xx, yy - yyy, z + zz) == VanillaMaterials.NETHERRACK.getId()) {
							blockData.set(x + xx, yy - yyy, z + zz, material.getId());
						} else {
							break;
						}
					}
				}
			}
		}
	}

	private int getHighestWorkableBlock(CuboidShortBuffer blockData, int x, int y, int z) {
		while (blockData.get(x, y, z) != VanillaMaterials.NETHERRACK.getId()) {
			y--;
			if (y <= 0) {
				return -1;
			}
		}
		return y;
	}
}
