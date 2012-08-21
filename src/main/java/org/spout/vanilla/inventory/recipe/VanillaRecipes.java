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
package org.spout.vanilla.inventory.recipe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.api.Spout;
import org.spout.api.inventory.Recipe;

import org.spout.vanilla.resources.RecipeYaml;

public class VanillaRecipes {
	private static final Map<String, Recipe> yamlRecipes = new ConcurrentHashMap<String, Recipe>();

	public static void initialize() {
		yamlRecipes.clear();
		for (String key : RecipeYaml.DEFAULT.getRecipes().keySet()) {
			Recipe recipe = RecipeYaml.DEFAULT.getRecipes().get(key);
			Spout.getEngine().getRecipeManager().addRecipe(recipe);
			yamlRecipes.put(key, recipe);
		}
	}

	public static Recipe get(String name) {
		return yamlRecipes.get(name);
	}
}
