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
package org.spout.vanilla.protocol.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.spout.api.component.components.PhysicsComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.misc.HeadComponent;
import org.spout.vanilla.protocol.msg.entity.EntityDestroyMessage;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityRelativePositionYawMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityVelocityMessage;
import org.spout.vanilla.protocol.msg.entity.pos.EntityYawMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

public abstract class VanillaEntityProtocol implements EntityProtocol {
	private List<Parameter<?>> lastMeta;

	public List<Parameter<?>> getUpdateParameters(Entity entity) {
		return Collections.emptyList();
	}

	@Override
	public final List<Message> getDestroyMessages(Entity entity) {
		return Arrays.<Message>asList(new EntityDestroyMessage(new int[]{entity.getId()}));
	}

	@Override
	public final List<Message> getUpdateMessages(Entity entity) {

		// Movement

		Transform prevTransform = entity.getTransform().getTransform();
		Transform newTransform = entity.getTransform().getTransformLive();

		int lastX = protocolifyPosition(prevTransform.getPosition().getX());
		int lastY = protocolifyPosition(prevTransform.getPosition().getY());
		int lastZ = protocolifyPosition(prevTransform.getPosition().getZ());

		int newX = protocolifyPosition(newTransform.getPosition().getX());
		int newY = protocolifyPosition(newTransform.getPosition().getY());
		int newZ = protocolifyPosition(newTransform.getPosition().getZ());
		int newYaw = protocolifyRotation(newTransform.getRotation().getYaw());
		int newPitch = protocolifyRotation(newTransform.getRotation().getPitch());

		int deltaX = newX - lastX;
		int deltaY = newY - lastY;
		int deltaZ = newZ - lastZ;

		List<Message> messages = new ArrayList<Message>();

		boolean looked = !entity.getTransform().isRotationDirty();

		/*
		 * Two scenarios:
		 * - The entity moves more than 4 blocks and maybe changes rotation.
		 * - The entity moves less than 4 blocks and maybe changes rotation.
		 */
		if (deltaX > 4 || deltaX < -4 || deltaY > 4 || deltaY < -4 || deltaZ > 4 || deltaZ < -4) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
			if (looked) {
				messages.add(new EntityYawMessage(entity.getId(), newYaw, newPitch));
			}
		} else {
			if (looked) {
				messages.add(new EntityRelativePositionYawMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
			} else {
				messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
			}
		}

		// Head movement

		HeadComponent head = entity.get(HeadComponent.class);
		if (head != null && head.isDirty()) {
			messages.add(new EntityHeadYawMessage(entity.getId(), head.getProtocolYaw()));
		}

		// Physics

		PhysicsComponent physics = entity.get(PhysicsComponent.class);
		if (physics != null && physics.isVelocityDirty()) {
			messages.add(new EntityVelocityMessage(entity.getId(), getProtocolVelocity(physics.getLinearVelocity())));
		}

		// Extra metadata

		List<Parameter<?>> params = getUpdateParameters(entity);
		if (lastMeta == null || !lastMeta.equals(params)) {
			messages.add(new EntityMetadataMessage(entity.getId(), params));
			lastMeta = params;
		}

		return messages;
	}

	public static Vector3 getProtocolVelocity(Vector3 velocity) {
		final float x = velocity.getX() * 32000;
		final float y = velocity.getY() * 32000;
		final float z = velocity.getZ() * 32000;
		return new Vector3(x, y, z);
	}
}
