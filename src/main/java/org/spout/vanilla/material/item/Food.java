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
package org.spout.vanilla.material.item;

import java.lang.reflect.InvocationTargetException;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.special.InventorySlot;

import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Food extends VanillaItemMaterial {
	private final FoodEffect[] effects;

	public Food(String name, int id, FoodEffect... effects) {
		super(name, id);
		this.effects = effects;
	}

	public FoodEffect[] getEffectType() {
		return effects;
	}

	public void onEat(Entity entity, InventorySlot slot) {
		if (VanillaPlayerUtil.isSurvival(entity)) {
			System.out.println("EATING");
			for (int i = 0; i < effects.length; i++) {
				VanillaPlayerController vPlayer = (VanillaPlayerController) entity.getController();
				try {
					effects[i].run(vPlayer);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			slot.addItemAmount(-1);
		}
	}
}
