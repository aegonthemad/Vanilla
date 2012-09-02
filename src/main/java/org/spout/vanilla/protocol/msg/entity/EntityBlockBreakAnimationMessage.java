package org.spout.vanilla.protocol.msg.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spout.api.util.SpoutToStringStyle;
import org.spout.vanilla.protocol.msg.EntityMessage;

public class EntityBlockBreakAnimationMessage extends EntityMessage {
	private final int x;
	private final int y;
	private final int z;
	private final byte destroyStage;

	public EntityBlockBreakAnimationMessage(int id, int x, int y, int z, byte destroyStage) {
		super(id);
		this.x = x;
		this.y = y;
		this.z = z;
		this.destroyStage = destroyStage;
	}
		
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public byte getDestroyStage() {
		return destroyStage;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("destroyStage", destroyStage)
				.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EntityBlockBreakAnimationMessage other = (EntityBlockBreakAnimationMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.destroyStage, other.destroyStage)
				.isEquals();
	}
	
}
