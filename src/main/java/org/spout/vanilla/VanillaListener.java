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
package org.spout.vanilla;

import java.util.HashSet;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.event.world.RegionLoadEvent;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.scheduler.TaskPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfigurationNode;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.creature.hostile.Ghast;
import org.spout.vanilla.entity.creature.passive.Sheep;
import org.spout.vanilla.entity.world.RegionSpawner;
import org.spout.vanilla.event.player.PlayerDeathEvent;
import org.spout.vanilla.material.VanillaMaterials;

public class VanillaListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onRegionLoad(RegionLoadEvent event) {
		Region region = event.getRegion();

		RegionSpawner spawner = new RegionSpawner(region);
		int taskId = region.getTaskManager().scheduleSyncRepeatingTask(plugin, spawner, 100, 100, TaskPriority.LOW);
		spawner.setTaskId(taskId);
		spawner.setTaskManager(region.getTaskManager());

		WorldConfigurationNode worldConfig = VanillaConfiguration.WORLDS.getOrCreate(event.getWorld());
		if (worldConfig.SPAWN_ANIMALS.getBoolean()) {
			HashSet<BlockMaterial> grass = new HashSet<BlockMaterial>();
			grass.add(VanillaMaterials.GRASS);
			spawner.addSpawnableType(VanillaControllerTypes.SHEEP, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.PIG, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.COW, grass, 5);

			spawner.addSpawnableType(VanillaControllerTypes.CHICKEN, grass, 5);
		}
		if (worldConfig.SPAWN_MONSTERS.getBoolean()) {
			HashSet<BlockMaterial> endStone = new HashSet<BlockMaterial>();
			endStone.add(VanillaMaterials.END_STONE);
			spawner.addSpawnableType(VanillaControllerTypes.ENDERMAN, endStone, 7);
		}
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPermissionNode(PermissionNodeEvent event) {
		if (VanillaConfiguration.OPS.isOp(event.getSubject().getName())) {
			event.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (VanillaConfiguration.HARDCORE_MODE.getBoolean()) {
			event.getPlayer().ban(true, ChatStyle.RED, "Game Over");
		}
	}
}
