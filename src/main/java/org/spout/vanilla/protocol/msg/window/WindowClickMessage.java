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
package org.spout.vanilla.protocol.msg.window;

import org.spout.api.inventory.ItemStack;

import org.spout.nbt.CompoundMap;
import org.spout.vanilla.protocol.msg.WindowMessage;
import org.spout.vanilla.window.Window;

public final class WindowClickMessage extends WindowMessage {
	private final int slot;
	private final boolean rightClick, shift;
	private final int transaction;
	private final CompoundMap item;

	public WindowClickMessage(
	        int windowInstanceId, 
	        int slot, 
	        boolean rightClick,
            int transaction, 
            boolean shift, 
            CompoundMap item) {     
	    super(windowInstanceId);
        this.slot = slot;
        this.rightClick = rightClick;
        this.transaction = transaction;
        this.shift = shift;
        this.item = item;
    }

    public int getSlot() {
		return slot;
	}

	public boolean isRightClick() {
		return rightClick;
	}

	public boolean isShift() {
		return shift;
	}

	public int getTransaction() {
		return transaction;
	}

	public CompoundMap getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "WindowClickMessage{id=" + this.getWindowInstanceId() + ",slot=" + slot + ",rightClick=" + rightClick + ",shift=" + shift + ",transaction=" + transaction + ",item=" + item + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WindowClickMessage other = (WindowClickMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getWindowInstanceId(), other.getWindowInstanceId())
				.append(this.slot, other.slot)
				.append(this.rightClick, other.rightClick)
				.append(this.shift, other.shift)
				.append(this.transaction, other.transaction)
				.append(this.item, other.item)
				.isEquals();
	}
}
