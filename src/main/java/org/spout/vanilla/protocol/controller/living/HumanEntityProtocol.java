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
package org.spout.vanilla.protocol.controller.living;

import java.util.ArrayList;
import java.util.List;

import net.royawesome.jlibnoise.MathHelper;

import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.protocol.controller.VanillaEntityProtocol;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnPlayerMessage;

public class HumanEntityProtocol extends VanillaEntityProtocol {
	private static final int MC_FULL_AIR = 300;

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null) {
			return null;
		}
		int id = entity.getId();
		int x = (int) (entity.getPosition().getX() * 32);
		int y = (int) (entity.getPosition().getY() * 32);
		int z = (int) (entity.getPosition().getZ() * 32);
		int r = (int) (-entity.getYaw() * 32); //cardinal directions differ
		int p = (int) (entity.getPitch() * 32);

		if (c instanceof Human) {
			Human mcp = (Human) c;
			int item = 0;
			ItemStack hand = mcp.getRenderedItemInHand();
			if (hand != null) {
				item = hand.getMaterial().getId();
			}

			int percentAirLeft = 100 - (mcp.getAirTicks() * 100 / mcp.getMaxAirTicks());
			int airLeft = MathHelper.clamp(percentAirLeft * MC_FULL_AIR, 0, MC_FULL_AIR);

			List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
			parameters.add(new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) airLeft));

			return new Message[]{new EntitySpawnPlayerMessage(id, mcp.getTitle(), x, y, z, r, p, item, parameters)};
		}

		return null;
	}
}
