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
package org.spout.vanilla.inventory.player;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.inventory.CraftingInventory;

public class PlayerCraftingGrid extends CraftingInventory {
	private static final int ROW_SIZE = 2, COLUMN_SIZE = 2;
	private static final long serialVersionUID = 1L;

	public PlayerCraftingGrid() {
		super(COLUMN_SIZE, ROW_SIZE);
	}

	/**
	 * Returns the current {@link ItemStack} in the top left input in the crafting grid slot (slot 42) ; can return null.
	 * @return top left input item stack
	 */
	public ItemStack getTopLeftInput() {
		return this.getItem(0);
	}

	/**
	 * Returns the current {@link ItemStack} in the top right input in the crafting grid slot (slot 43) ; can return null.
	 * @return top right item stack
	 */
	public ItemStack getTopRightInput() {
		return this.getItem(1);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom left input in the crafting grid slot (slot 38) ; can return null.
	 * @return bottom left input item stack
	 */
	public ItemStack getBottomLeftInput() {
		return this.getItem(2);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom right input in the crafting grid slot (slot 39) ; can return null.
	 * @return bottom right input item stack
	 */
	public ItemStack getBottomRightInput() {
		return this.getItem(3);
	}
}
