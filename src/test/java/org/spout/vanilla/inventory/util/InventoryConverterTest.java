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
package org.spout.vanilla.inventory.util;

import org.junit.Test;

import org.spout.api.inventory.Inventory;

import org.spout.vanilla.inventory.util.GridInventoryConverter;
import org.spout.vanilla.inventory.util.InventoryConverter;

import static org.junit.Assert.assertEquals;

public class InventoryConverterTest {
	private final Inventory main = new Inventory(27);

	@Test
	public void testGridInventoryConverter() {
		GridInventoryConverter converter = new GridInventoryConverter(main, 9, 9);
		for (int a = 0; a < 9; a++) {
			assertEquals(a, converter.convert(a + 27));
			assertEquals(a + 27, converter.revert(a));
		}
	}

	@Test
	public void testInventoryConverter() {
		InventoryConverter converter = new InventoryConverter(main, "27-35");
		for (int a = 0; a < 9; a++) {
			assertEquals(a, converter.convert(a + 27));
			assertEquals(a + 27, converter.revert(a));
		}
	}
}
