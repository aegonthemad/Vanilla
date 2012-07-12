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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.special.InventoryRange;
import org.spout.api.inventory.special.InventorySlot;

public class CraftingInventory extends Inventory implements VanillaInventory {
	private static final long serialVersionUID = 1L;
	private final int columns, rows;
	private final InventorySlot output;
	private final InventoryRange grid;

	public CraftingInventory(int columns, int rows) {
		super(columns * rows + 1);
		this.columns = columns;
		this.rows = rows;
		this.grid = this.createRange(0, this.getSize() - 1);
		this.output = this.createSlot(this.getSize() - 1);
	}

	/**
	 * Gets the output slot in the grid.
	 * @return output slot
	 */
	public InventorySlot getOutput() {
		return this.output;
	}

	/**
	 * Gets the row size of the grid.
	 * @return row size
	 */
	public int getRowSize() {
		return this.rows;
	}

	/**
	 * Gets the column size of the grid.
	 * @return column size
	 */
	public int getColumnSize() {
		return this.columns;
	}

	/**
	 * Gets the grid contained in this inventory
	 * @return grid inventory range
	 */
	public InventoryRange getGrid() {
		return this.grid;
	}
}
