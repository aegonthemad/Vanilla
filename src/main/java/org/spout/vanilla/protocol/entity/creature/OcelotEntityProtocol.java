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
package org.spout.vanilla.protocol.entity.creature;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.living.passive.Ocelot;

public class OcelotEntityProtocol extends CreatureProtocol {
	public final static int SIT_TAME_INDEX = 16; // The MC metadata index for determining whether the ocelot is tamed or sitting.
	public final static int OWNER_INDEX = 17; // The MC metadata index for the owner's name of the ocelot.
	public final static int SKIN_INDEX = 18; // The MC metadata index for the ocelots texture.

	public OcelotEntityProtocol() {
		super(CreatureType.OCELOT);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> parameters = super.getSpawnParameters(entity);
		Ocelot ocelot = entity.add(Ocelot.class);
		byte data = 0;
		data |= (ocelot.isSitting() ? 1 : 0) & 0x01;
		data |= (ocelot.isTamed() ? 1 : 0) & 0x04;
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, SIT_TAME_INDEX, data));
		parameters.add(new Parameter<String>(Parameter.TYPE_STRING, OWNER_INDEX, ocelot.getOwnerName()));
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, SKIN_INDEX, ocelot.getSkinId()));
		return parameters;
	}
}
