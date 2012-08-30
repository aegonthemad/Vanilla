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
package org.spout.vanilla.entity.block;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventorySlot;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.entity.VanillaBlockController;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.object.moving.Item;
import org.spout.vanilla.inventory.block.JukeboxInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.misc.MusicDisc;
import org.spout.vanilla.util.Music;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Jukebox extends VanillaBlockController {
	private final JukeboxInventory inventory;

	public Jukebox() {
		super(VanillaControllerTypes.JUKEBOX, VanillaMaterials.JUKEBOX);
		inventory = new JukeboxInventory();
	}

	@Override
	public void onAttached() {
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK) {
			this.eject();
			InventorySlot inv = VanillaPlayerUtil.getCurrentSlot(entity);
			if (inv != null && this.canPlay(inv.getItem())) {
				this.getInventory().addItem(inv.getItem().clone().setAmount(1));
				this.update();
				if (VanillaPlayerUtil.isSurvival(entity)) {
					inv.addItemAmount(0, -1);
				}
			} else {
				this.stopMusic();
			}
		}
	}

	/**
	 * Gets the current music this Jukebox plays
	 * @return
	 */
	public Music getMusic() {
		ItemStack current = inventory.getMusicSlot();
		if (!canPlay(current)) {
			return Music.NONE;
		}

		return ((MusicDisc) current.getMaterial()).getMusic();
	}

	/**
	 * Tests whether this Jukebox can play the item specified
	 * @param item to play
	 * @return True if it can play it, False if not
	 */
	public boolean canPlay(ItemStack item) {
		return item != null && this.canPlay(item.getMaterial());
	}

	/**
	 * Tests whether this Jukebox can play the item material specified
	 * @param material to play
	 * @return True if it can play it, False if not
	 */
	public boolean canPlay(Material material) {
		return material instanceof MusicDisc;
	}

	/**
	 * Ejects the currently playing music disc
	 */
	public void eject() {
		ItemStack current = this.inventory.getMusicSlot();
		this.inventory.setMusicSlot(null);
		if (current != null) {
			Point position = this.getParent().getPosition();
			position.getWorld().createAndSpawnEntity(position, new Item(current, Vector3.UP.multiply(0.5)));
			this.update();
		}
	}

	/**
	 * Updates the playing state of this Jukebox with the current item
	 */
	public void update() {
		this.setPlaying(true);
	}

	/**
	 * Sets whether this Jukebox is playing or not
	 * @param playing
	 */
	public void setPlaying(boolean playing) {
		Block block = this.getBlock();
		block.setData(playing ? 1 : 0); //TODO hmmm? doesn't seem useful at all, since you don't know when the music stops playing...
		Music music = playing ? this.getMusic() : Music.NONE;
		GeneralEffects.MUSIC_DISC.playGlobal(block.getPosition(), music);
	}

	public JukeboxInventory getInventory() {
		return inventory;
	}

	@Override
	public void onTick(float dt) {
	}

	/**
	 * Stops this Jukebox from playing music
	 */
	public void stopMusic() {
		GeneralEffects.MUSIC_DISC.playGlobal(getBlock().getPosition(), Music.NONE);
	}
}
