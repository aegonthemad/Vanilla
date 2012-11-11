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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.math.Vector3;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;

public class GroundCoverPopulator implements GeneratorPopulator {
	@Override
	public void populate(CuboidShortBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		final Random random = WorldGeneratorUtils.getRandom(seed, x, y, z, 13823);
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		for (int xx = 0; xx < sizeX; xx++) {
			for (int zz = 0; zz < sizeZ; zz++) {
				final Biome biome = biomes.getBiome(xx, 0, zz);
				if (!(biome instanceof NormalBiome)) {
					continue;
				}
				final GroundCoverLayer[] layers = ((NormalBiome) biome).getGroundCover();
				int yy = sizeY - 1;
				yIteration:
				while (yy >= 0) {
					yy = getNextStone(blockData, x + xx, y, z + zz, yy);
					if (yy < 0) {
						break yIteration;
					}
					int layerNumber = 0;
					for (GroundCoverLayer layer : layers) {
						final int depthY = yy - layer.getDepth(random);
						final BlockMaterial cover = layer.getMaterial(y + yy < NormalGenerator.SEA_LEVEL);
						for (; yy > depthY; yy--) {
							if (yy < 0) {
								break yIteration;
							}
							if (blockData.get(x + xx, y + yy, z + zz) == VanillaMaterials.STONE.getId()) {
								blockData.set(x + xx, y + yy, z + zz, cover.getId());
							}
						}
					}
					yy = getNextAir(blockData, x + xx, y, z + zz, yy);
					layerNumber++;
				}
			}
		}
	}

	private int getNextStone(CuboidShortBuffer blockData, int x, int y, int z, int yy) {
		for (; yy >= 0 && blockData.get(x, y + yy, z) != VanillaMaterials.STONE.getId(); yy--) {
			// iterate until we reach stone
		}
		return yy;
	}

	private int getNextAir(CuboidShortBuffer blockData, int x, int y, int z, int yy) {
		for (; yy >= 0 && blockData.get(x, y + yy, z) == VanillaMaterials.STONE.getId(); yy--) {
			// iterate until we exit the stone column
		}
		return yy;
	}

	public static abstract class GroundCoverLayer {
		private static final Map<String, GroundCoverLayerFactory> FACTORIES =
				new HashMap<String, GroundCoverLayerFactory>();
		private BlockMaterial aboveSea;
		private BlockMaterial bellowSea;

		public GroundCoverLayer(BlockMaterial aboveSea, BlockMaterial bellowSea) {
			this.aboveSea = aboveSea;
			this.bellowSea = bellowSea;
		}

		public abstract byte getDepth(Random random);

		public BlockMaterial getMaterial(boolean isBellowSea) {
			return isBellowSea ? bellowSea : aboveSea;
		}

		public abstract String getTypeName();

		public void load(ConfigurationNode node) {
			final ConfigurationNode materials = node.getNode("materials");
			aboveSea = (BlockMaterial) MaterialRegistry.get(materials.getNode("above-sea").
					getString(aboveSea.getDisplayName()));
			bellowSea = (BlockMaterial) MaterialRegistry.get(materials.getNode("bellow-sea").
					getString(bellowSea.getDisplayName()));
		}

		public void save(ConfigurationNode node) {
			node.getNode("type").setValue(getTypeName());
			final ConfigurationNode materials = node.getNode("materials");
			materials.getNode("above-sea").setValue(aboveSea.getDisplayName());
			materials.getNode("bellow-sea").setValue(bellowSea.getDisplayName());
		}

		public static void register(String name, GroundCoverLayerFactory facory) {
			FACTORIES.put(name, facory);
		}

		public static GroundCoverLayer loadNew(ConfigurationNode node) {
			return FACTORIES.get(node.getNode("type").getString()).make(node);
		}
	}

	public static class GroundCoverVariableLayer extends GroundCoverLayer {
		private byte minDepth;
		private byte maxDepth;

		static {
			register("variable", new GroundCoverLayerFactory() {
				@Override
				public GroundCoverLayer make(ConfigurationNode node) {
					final ConfigurationNode materials = node.getNode("materials");
					final BlockMaterial aboveSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("above-sea").getString());
					final BlockMaterial bellowSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("bellow-sea").getString());
					final ConfigurationNode depthNode = node.getNode("depth");
					final byte minDepth = depthNode.getNode("min").getByte();
					final byte maxDepth = depthNode.getNode("max").getByte();
					return new GroundCoverVariableLayer(aboveSea, bellowSea, minDepth, maxDepth);
				}
			});
		}

		public GroundCoverVariableLayer(BlockMaterial aboveSea, BlockMaterial bellowSea,
				byte minDepth, byte maxDepth) {
			super(aboveSea, bellowSea);
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
		}

		@Override
		public byte getDepth(Random random) {
			return (byte) (random.nextInt(maxDepth - minDepth + 1) + minDepth);
		}

		@Override
		public String getTypeName() {
			return "variable";
		}

		@Override
		public void load(ConfigurationNode node) {
			super.load(node);
			final ConfigurationNode depthNode = node.getNode("depth");
			minDepth = depthNode.getNode("min").getByte(minDepth);
			maxDepth = depthNode.getNode("max").getByte(maxDepth);
		}

		@Override
		public void save(ConfigurationNode node) {
			super.save(node);
			final ConfigurationNode depthNode = node.getNode("depth");
			depthNode.getNode("min").setValue(minDepth);
			depthNode.getNode("max").setValue(maxDepth);
		}
	}

	public static class GroundCoverUniformLayer extends GroundCoverLayer {
		private byte depth;

		static {
			register("uniform", new GroundCoverLayerFactory() {
				@Override
				public GroundCoverLayer make(ConfigurationNode node) {
					final ConfigurationNode materials = node.getNode("materials");
					final BlockMaterial aboveSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("above-sea").getString());
					final BlockMaterial bellowSea = (BlockMaterial) MaterialRegistry.
							get(materials.getNode("bellow-sea").getString());
					final byte depth = node.getNode("depth").getByte();
					return new GroundCoverUniformLayer(aboveSea, bellowSea, depth);
				}
			});
		}

		public GroundCoverUniformLayer(BlockMaterial aboveSea, BlockMaterial bellowSea,
				byte depth) {
			super(aboveSea, bellowSea);
			this.depth = depth;
		}

		@Override
		public byte getDepth(Random random) {
			return depth;
		}

		@Override
		public String getTypeName() {
			return "uniform";
		}

		@Override
		public void load(ConfigurationNode node) {
			super.load(node);
			depth = node.getNode("depth").getByte(depth);
		}

		@Override
		public void save(ConfigurationNode node) {
			super.save(node);
			node.getNode("depth").setValue(depth);
		}
	}

	public static interface GroundCoverLayerFactory {
		public GroundCoverLayer make(ConfigurationNode node);
	}
}
