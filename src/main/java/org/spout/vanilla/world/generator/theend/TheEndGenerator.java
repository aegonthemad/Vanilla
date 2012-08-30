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
package org.spout.vanilla.world.generator.theend;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.combiner.Add;
import net.royawesome.jlibnoise.module.combiner.Multiply;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.BiomePopulator;
import org.spout.api.generator.biome.selector.PerBlockBiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomeChunkGenerator;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.VanillaGenerator;

public class TheEndGenerator extends VanillaBiomeChunkGenerator implements VanillaGenerator {
	public static final int HEIGHT = 128;
	private static final int SEA_LEVEL = 63;
	private static final int ISLAND_HEIGHT = 56;
	private static final int ISLAND_OFFSET = 8;
	private static final int ISLAND_RADIUS = 144;
	private static final float ISLAND_TOTAL_OFFSET = ISLAND_OFFSET + ISLAND_HEIGHT / 2f;
	private static final float ISLAND_HEIGHT_SCALE = ((float) ISLAND_RADIUS / (float) ISLAND_HEIGHT) * 2f;
	// noise for generation
	private static final Perlin BASE = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	private static final Turbulence TURBULENCE = new Turbulence();
	private static final ScalePoint SCALE = new ScalePoint();
	private static final Clamp FINAL = new Clamp();

	static {
		BASE.setFrequency(0.012);
		BASE.setLacunarity(1);
		BASE.setNoiseQuality(NoiseQuality.STANDARD);
		BASE.setPersistence(0.7);
		BASE.setOctaveCount(1);

		ROUGHNESS.setFrequency(0.0318);
		ROUGHNESS.setLacunarity(1);
		ROUGHNESS.setNoiseQuality(NoiseQuality.STANDARD);
		ROUGHNESS.setPersistence(0.9);
		ROUGHNESS.setOctaveCount(1);

		DETAIL.setFrequency(0.042);
		DETAIL.setLacunarity(1);
		DETAIL.setNoiseQuality(NoiseQuality.STANDARD);
		DETAIL.setPersistence(0.7);
		DETAIL.setOctaveCount(1);

		final Multiply multiply = new Multiply();
		multiply.SetSourceModule(0, ROUGHNESS);
		multiply.SetSourceModule(1, DETAIL);

		final Add add = new Add();
		add.SetSourceModule(0, multiply);
		add.SetSourceModule(1, BASE);

		TURBULENCE.SetSourceModule(0, add);
		TURBULENCE.setFrequency(0.01);
		TURBULENCE.setPower(8);
		TURBULENCE.setRoughness(1);

		SCALE.SetSourceModule(0, TURBULENCE);
		SCALE.setxScale(0.7);
		SCALE.setyScale(1);
		SCALE.setzScale(0.7);
		
		FINAL.SetSourceModule(0, SCALE);
		FINAL.setLowerBound(-1);
		FINAL.setUpperBound(1);
	}

	public TheEndGenerator() {
		super(HEIGHT, VanillaBiomes.ENDSTONE);
	}

	@Override
	public void registerBiomes() {
		setSelector(new PerBlockBiomeSelector(VanillaBiomes.ENDSTONE));
		addPopulators(new BiomePopulator(getBiomeMap()));
		register(VanillaBiomes.ENDSTONE);
	}

	@Override
	public String getName() {
		return "VanillaTheEnd";
	}

	@Override
	protected void generateTerrain(CuboidShortBuffer blockData, int x, int y, int z) {
		if (x < -ISLAND_RADIUS || x > ISLAND_RADIUS
				|| y < 0 || y > ISLAND_HEIGHT + ISLAND_OFFSET
				|| z < -ISLAND_RADIUS || z > ISLAND_RADIUS) {
			return;
		}
		final int seed = (int) blockData.getWorld().getSeed();
		BASE.setSeed(seed * 23);
		ROUGHNESS.setSeed(seed * 29);
		DETAIL.setSeed(seed * 17);
		TURBULENCE.setSeed(seed * 53);
		final int size = blockData.getSize().getFloorX();
		final double[][][] densityNoise = WorldGeneratorUtils.fastNoise(FINAL, size, size, size, 4, x, y, z);
		for (int xx = 0; xx < size; xx++) {
			for (int yy = 0; yy < size; yy++) {
				for (int zz = 0; zz < size; zz++) {
					final int totalX = x + xx;
					final int totalY = y + yy;
					final float distanceY = (totalY - ISLAND_TOTAL_OFFSET) * ISLAND_HEIGHT_SCALE;
					final int totalZ = z + zz;
					final double distance = Math.sqrt(totalX * totalX + distanceY * distanceY + totalZ * totalZ);
					if (distance == 0) {
						blockData.set(totalX, totalY, totalZ, VanillaMaterials.END_STONE.getId());
						continue;
					}
					final double distanceDensity = (ISLAND_RADIUS / distance) / ISLAND_RADIUS;
					final double density = distanceDensity * (densityNoise[xx][yy][zz] * 0.5 + 0.5);
					if (density >= 1d / ISLAND_RADIUS) {
						blockData.set(totalX, totalY, totalZ, VanillaMaterials.END_STONE.getId());
					}
				}
			}
		}
	}

	@Override
	public Point getSafeSpawn(World world) {
		final Random random = new Random();
		for (byte attempts = 0; attempts < 10; attempts++) {
			final int x = random.nextInt(32) - 16;
			final int z = random.nextInt(32) - 16;
			final int y = getHighestSolidBlock(world, x, z);
			if (y != -1) {
				return new Point(world, x, y + 0.5f, z);
			}
		}
		return new Point(world, 0, 80, 0);
	}

	private int getHighestSolidBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return ++y;
	}

	@Override
	public int[][] getSurfaceHeight(World world, int chunkX, int chunkY) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = SEA_LEVEL;
			}
		}
		return heights;
	}
}
