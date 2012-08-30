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
package org.spout.vanilla.item;

import org.junit.Test;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.armor.Armor;
import org.spout.vanilla.util.EnchantmentUtil;

import static org.junit.Assert.assertTrue;

public class DamageTest {
	@Test
	public void testDamageModifier() {
		ItemStack test = new ItemStack(VanillaMaterials.DIAMOND_CHESTPLATE, 1);
		EnchantmentUtil.addEnchantment(test, Enchantments.PROTECTION, 4, false);
		assertTrue(EnchantmentUtil.hasEnchantment(test, Enchantments.PROTECTION));

		Armor armor = (Armor) test.getMaterial();
		assertTrue((int) Math.ceil(.04 * (armor.getBaseProtection() + armor.getProtection(test, DamageCause.CACTUS))) == 1);
	}
}
