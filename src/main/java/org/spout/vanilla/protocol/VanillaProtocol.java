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
package org.spout.vanilla.protocol;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.chat.ChatArguments;
import org.spout.api.command.Command;
import org.spout.api.entity.component.Controller;
import org.spout.api.exception.UnknownPacketException;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.common.message.CustomDataMessage;
import org.spout.api.util.Named;

import org.spout.vanilla.chat.style.VanillaStyleHandler;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.ControllerChangeReason;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.KickMessage;

public class VanillaProtocol extends Protocol {
	public final static DefaultedKey<String> SESSION_ID = new DefaultedKeyImpl<String>("sessionid", "0000000000000000");
	public final static DefaultedKey<String> HANDSHAKE_USERNAME = new DefaultedKeyImpl<String>("handshake_username", "");
	public final static DefaultedKey<Long> LOGIN_TIME = new DefaultedKeyImpl<Long>("handshake_time", -1L);
	public static final int DEFAULT_PORT = 25565;

	public VanillaProtocol() {
		super("Vanilla", DEFAULT_PORT, new VanillaCodecLookupService(), new VanillaHandlerLookupService());
	}

	@Override
	public Message getCommandMessage(Command command, ChatArguments args) {
		if (command.getPreferredName().equals("kick")) {
			return getKickMessage(args);
		} else if (command.getPreferredName().equals("say")) {
			return new ChatMessage(args.asString(VanillaStyleHandler.ID) + "\u00a7r"); // The reset text is a workaround for a change in 1.3 -- Remove if fixed
		} else {
			return new ChatMessage('/' + command.getPreferredName() + ' ' + args.asString(VanillaStyleHandler.ID));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Message> Message getWrappedMessage(T dynamicMessage) throws IOException {
		MessageCodec<T> codec = (MessageCodec<T>) getCodecLookupService().find(dynamicMessage.getClass());
		ChannelBuffer buffer = codec.encode(dynamicMessage);

		return new CustomDataMessage(getName(codec), buffer.array());
	}
	
	@Override
	public MessageCodec<?> readHeader(ChannelBuffer buf) throws UnknownPacketException {
		int opcode = buf.readUnsignedByte();
		MessageCodec<?> codec = getCodecLookupService().find(opcode);
		if (codec == null) {
			throw new UnknownPacketException(opcode);
		}
		return codec;
	}

	@Override
	public ChannelBuffer writeHeader(MessageCodec<?> codec, ChannelBuffer data) {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		buffer.writeByte(codec.getOpcode());
		return buffer;
	}

	@Override
	public Message getKickMessage(ChatArguments message) {
		return new KickMessage(message.asString(VanillaStyleHandler.ID));
	}

	@Override
	public Message getIntroductionMessage(String playerName) {
		//return new HandshakeMessage(VanillaPlugin.MINECRAFT_PROTOCOL_ID, playerName); //TODO Fix this Raphfrk
		return null;
	}

	private String getName(MessageCodec<?> codec) {
		if (codec instanceof Named) {
			return ((Named) codec).getName();
		} else {
			return "Spout-" + codec.getOpcode();
		}
	}

	@Override
	public void initializeSession(Session session) {
		final Player player = session.getPlayer();
		session.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, player));

		Controller controller = player.getController();

		if (controller instanceof VanillaPlayer) {
			VanillaPlayer vanillaPlayer = (VanillaPlayer) controller;
			// Set protocol and send packets
			if (vanillaPlayer.isSurvival()) {
				vanillaPlayer.updateHealth();
			}
		}

		StringBuilder listBuilder = new StringBuilder();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				if (listBuilder.length() > 0) {
					listBuilder.append('\0');
				}
				System.out.println(codec);
				listBuilder.append(getName(codec));
			} else {
				throw new IllegalArgumentException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in CodecLookupService!");
			}
		}

		session.send(false, new CustomDataMessage("REGISTER", listBuilder.toString().getBytes(ChannelBufferUtils.CHARSET_UTF8)));
	}

	@Override
	public void setPlayerController(Player player) {
		VanillaPlayer vanillaPlayer = new VanillaPlayer(player.getWorld().getDataMap().get(VanillaData.GAMEMODE));
		vanillaPlayer.setTitle(player.getDisplayName());

		player.setController(vanillaPlayer, ControllerChangeReason.INITIALIZATION);
	}
}
