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
package org.spout.vanilla.command;

import java.util.Set;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.plugin.Platform;
import org.spout.api.protocol.NetworkSynchronizer;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.configuration.OpConfiguration;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.Weather;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.entity.world.VanillaSky;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.VanillaBlockUtil;

public class AdministrationCommands {
	private final VanillaPlugin plugin;

	public AdministrationCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"tp", "teleport"}, usage = "[player] [player|x] [y] [z] [-w <world>]", flags = "w:", desc = "Teleport to a location", min = 1, max = 4)
	@CommandPermissions("vanilla.command.tp")
	public void tp(CommandContext args, CommandSource source) throws CommandException {
		int index = 0;
		Player player;

		if (args.length() % 2 == 0) {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			player = ((Server) Spout.getEngine()).getPlayer(args.getString(index++), true);

			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to teleport yourself!");
			}

			player = (Player) source;
		}

		Point point;

		if (args.length() > 2) {
			World world = player.getWorld();

			if (args.hasFlag('w')) {
				if (!source.hasPermission("vanilla.command.tp.world-flag")) {
					throw new CommandException("You are not allowed to use the world flag.");
				}

				world = Spout.getEngine().getWorld(args.getFlagString('w'));

				if (world == null) {
					throw new CommandException("Please supply an existing world.");
				}
			}
			
			point = new Point(world, args.getInteger(index), args.getInteger(index + 1), args.getInteger(index + 2));
		} else {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			Player target = ((Server) Spout.getEngine()).getPlayer(args.getString(index), true);

			if (target == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}

			point = target.getPosition();
		}

		point.getWorld().getChunkFromBlock(point);
		player.setPosition(point);
		player.getNetworkSynchronizer().setPositionDirty();
	}

	@Command(aliases = {"give"}, usage = "[player] <block> [amount] ", desc = "Lets a player spawn items", min = 1, max = 3)
	@CommandPermissions("vanilla.command.give")
	public void give(CommandContext args, CommandSource source) throws CommandException {
		int index = 0;
		Player player = null;

		if (args.length() != 1) {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			player = ((Server) Spout.getEngine()).getPlayer(args.getString(index++), true);
		}

		if (player == null) {
			switch (args.length()) {
				case 3:
					throw new CommandException(args.getString(0) + " is not online.");
				case 2:
					index--;
				case 1:
					if (!(source instanceof Player)) {
						throw new CommandException("You must be a player to give yourself materials!");
					}

					player = (Player) source;
					break;
			}
		}

		Material material;
		VanillaPlayerController controller = (VanillaPlayerController) player.getController();

		if (args.isInteger(index)) {
			material = VanillaMaterials.getMaterial((short) args.getInteger(index));
		} else {
			String name = args.getString(index);

			if (name.contains(":")) {
				String[] parts = args.getString(index).split(":");
				material = VanillaMaterials.getMaterial(Short.parseShort(parts[0]), Short.parseShort(parts[1]));
			} else {
				material = Material.get(args.getString(index));
			}
		}

		if (material == null) {
			throw new CommandException(args.getString(index) + " is not a block!");
		}

		int count = args.getInteger(++index, 1);

		controller.getInventory().getMain().addItem(new ItemStack(material, count));

		source.sendMessage("Gave ", controller.getParent().getName(), " ", count, " ", material.getDisplayName());
	}

	@Command(aliases = {"deop"}, usage = "<player>", desc = "Revoke a players operator status", min = 1, max = 1)
	@CommandPermissions("vanilla.command.deop")
	public void deop(CommandContext args, CommandSource source) throws CommandException {
		Platform platform = Spout.getPlatform();
		if (platform != Platform.SERVER || platform != Platform.PROXY) {
			throw new CommandException("You cannot search for players unless you are in server mode.");
		}

		String playerName = args.getString(0);
		OpConfiguration ops = VanillaConfiguration.OPS;
		ops.setOp(playerName, false);
		source.sendMessage(ChatStyle.YELLOW, playerName, " had their operator status revoked!");
		Player player = ((Server) Spout.getEngine()).getPlayer(playerName, true);
		if (player != null && !source.equals(player)) {
			player.sendMessage(ChatStyle.YELLOW, "You had your operator status revoked!");
		}
	}

	@Command(aliases = {"op"}, usage = "<player>", desc = "Make a player an operator", min = 1, max = 1)
	@CommandPermissions("vanilla.command.op")
	public void op(CommandContext args, CommandSource source) throws CommandException {
		Platform platform = Spout.getPlatform();
		if (platform != Platform.SERVER || platform != Platform.PROXY) {
			throw new CommandException("You cannot search for players unless you are in server mode.");
		}

		String playerName = args.getString(0);
		OpConfiguration ops = VanillaConfiguration.OPS;
		ops.setOp(playerName, true);
		source.sendMessage(ChatStyle.YELLOW, playerName, " is now an operator!");
		Player player = ((Server) Spout.getEngine()).getPlayer(playerName, true);
		if (player != null && !source.equals(player)) {
			player.sendMessage(ChatStyle.YELLOW, "You are now an operator!");
		}
	}

	public enum Times {
		DAWN(0),
		DAY(6000),
		DUSK(12000),
		NIGHT(18000);
		private int time;

		Times(int time) {
			this.time = time;
		}

		public int getTime() {
			return time;
		}

		public static Times get(String name) {
			return valueOf(name.toUpperCase());
		}
	}

	@Command(aliases = {"time"}, usage = "<add|set> <0-24000|day|night|dawn|dusk> [world]", desc = "Set the time of the server", min = 2, max = 3)
	@CommandPermissions("vanilla.command.time")
	public void time(CommandContext args, CommandSource source) throws CommandException {
		int time = 0;
		boolean relative = false;
		if (args.getString(0).equalsIgnoreCase("set")) {
			if (args.isInteger(1)) {
				time = args.getInteger(1);
			} else {
				try {
					time = Times.get(args.getString(1)).getTime();
				} catch (Exception e) {
					throw new CommandException("'" + args.getString(1) + "' is not a valid time.");
				}
			}
		} else if (args.getString(0).equalsIgnoreCase("add")) {
			relative = true;
			if (args.isInteger(1)) {
				time = args.getInteger(1);
			} else {
				throw new CommandException("Argument to 'add' must be an integer.");
			}
		}

		World world;
		if (args.length() == 3) {
			world = plugin.getEngine().getWorld(args.getString(2));
			if (world == null) {
				throw new CommandException("'" + args.getString(2) + "' is not a valid world.");
			}
		} else if (source instanceof Player) {
			Player player = (Player) source;
			world = player.getWorld();
		} else {
			throw new CommandException("You must specify a world.");
		}

		VanillaSky sky = VanillaSky.getSky(world);
		if (sky == null) {
			throw new CommandException("The world '" + args.getString(2) + "' is not available.");
		}

		sky.setTime(relative ? (sky.getTime() + time) : time);
		source.sendMessage("Set ", world.getName(), "'s time to: ", sky.getTime());
	}

	@Command(aliases = {"gamemode", "gm"}, usage = "[player] <0|1|2|survival|creative|adventure> (0 = SURVIVAL, 1 = CREATIVE, 2 = ADVENTURE)", desc = "Change a player's game mode", min = 1, max = 2)
	@CommandPermissions("vanilla.command.gamemode")
	public void gamemode(CommandContext args, CommandSource source) throws CommandException {
		int index = 0;
		Player player;
		if (args.length() == 2) {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			player = ((Server) Spout.getEngine()).getPlayer(args.getString(index++), true);
			if (player == null) {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		} else {
			if (!(source instanceof Player)) {
				throw new CommandException("You must be a player to toggle your game mode.");
			}

			player = (Player) source;
		}

		if (!(player.getController() instanceof VanillaPlayerController)) {
			throw new CommandException("Invalid player!");
		}

		VanillaPlayerController controller = (VanillaPlayerController) player.getController();

		GameMode mode;

		try {
			if (args.isInteger(index)) {
				mode = GameMode.get(args.getInteger(index));
			} else {
				mode = GameMode.get(args.getString(index));
			}
		} catch (Exception e) {
			throw new CommandException("A game mode must be either a number between 0 and 2, 'CREATIVE', 'SURVIVAL' or 'ADVENTURE'");
		}

		controller.setGameMode(mode);

		if (!player.equals(source)) {
			source.sendMessage(controller.getParent().getName(), "'s game mode has been changed to ", mode.name(), ".");
		}
	}

	@Command(aliases = "xp", usage = "[player] <amount>", desc = "Give/take experience from a player", min = 1, max = 2)
	@CommandPermissions("vanilla.command.xp")
	public void xp(CommandContext args, CommandSource source) throws CommandException {
		// If source is player
		if (args.length() == 1) {
			if (source instanceof Player) {
				@SuppressWarnings("unused")
				Player sender = (Player) source;
				int amount = args.getInteger(0);
				source.sendMessage("You have been given ", amount, " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException("You must be a player to give yourself xp.");
			}
		} else {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			Player player = ((Server) Spout.getEngine()).getPlayer(args.getString(0), true);
			if (player != null) {
				int amount = args.getInteger(1);
				player.sendMessage("You have been given ", amount, " xp.");
				// TODO: Give player 'amount' of xp.
			} else {
				throw new CommandException(args.getString(0) + " is not online.");
			}
		}
	}

	@Command(aliases = "weather", usage = "<0|1|2> (0 = CLEAR, 1 = RAIN/SNOW, 2 = THUNDERSTORM) [world]", desc = "Changes the weather", min = 1, max = 2)
	@CommandPermissions("vanilla.command.weather")
	public void weather(CommandContext args, CommandSource source) throws CommandException {
		World world;
		if (source instanceof Player && args.length() == 1) {
			world = ((Player) source).getWorld();
		} else if (args.length() == 2) {
			world = plugin.getEngine().getWorld(args.getString(1));

			if (world == null) {
				throw new CommandException("Invalid world '" + args.getString(1) + "'.");
			}
		} else {
			throw new CommandException("You need to specify a world.");
		}

		Weather weather;
		try {
			if (args.isInteger(0)) {
				weather = Weather.get(args.getInteger(0));
			} else {
				weather = Weather.get(args.getString(0).replace("snow", "rain"));
			}
		} catch(Exception e) {
			throw new CommandException("Weather must be a mode between 0 and 2, 'CLEAR', 'RAIN', 'SNOW', or 'THUNDERSTORM'");
		}

		VanillaSky sky = VanillaSky.getSky(world);
		if (sky == null) {
			throw new CommandException("The sky of world '" + world.getName() + "' is not availible.");
		}

		sky.setWeather(weather);

		switch (weather) {
			case RAIN:
				source.sendMessage("Weather set to RAIN/SNOW.");
				break;
			default:
				source.sendMessage("Weather set to ", weather.name(), ".");
				break;
		}
	}

	@Command(aliases = "debug", usage = "[type] (/resend /resendall)", desc = "Debug commands", max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void debug(CommandContext args, CommandSource source) throws CommandException {
		Player player;
		if (source instanceof Player) {
			player = (Player) source;
		} else {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			player = ((Server) Spout.getEngine()).getPlayer(args.getString(1, ""), true);
			if (player == null) {
				source.sendMessage("Must be a player or send player name in arguments");
				return;
			}
		}

		if (args.getString(0, "").contains("resendall")) {
			NetworkSynchronizer network = player.getNetworkSynchronizer();
			Set<Chunk> chunks = network.getActiveChunks();
			for (Chunk c : chunks) {
				network.sendChunk(c);
			}

			source.sendMessage("All chunks resent");
		} else if (args.getString(0, "").contains("resend")) {
			player.getNetworkSynchronizer().sendChunk(player.getChunk());
			source.sendMessage("Chunk resent");
		} else if (args.getString(0, "").contains("relight")) {
			for (Chunk chunk : VanillaBlockUtil.getChunkColumn(player.getChunk())) {
				chunk.initLighting();
			}
			source.sendMessage("Chunk lighting is being initialized");
		}
	}

	@Command(aliases = {"kill"}, usage = "[player]", desc = "Kill yourself or another player", min = 0, max = 1)
	@CommandPermissions("vanilla.command.kill")
	public void kill(CommandContext args, CommandSource source) throws CommandException {
		if (args.length() == 0) {
			if (!(source instanceof Player)) {
				throw new CommandException("Don't be silly...you cannot kill yourself as the console.");
			}
			((VanillaPlayerController) ((Player) source).getController()).getHealth().die(source);
		} else {
			Platform platform = Spout.getPlatform();
			if (platform != Platform.SERVER || platform != Platform.PROXY) {
				throw new CommandException("You cannot search for players unless you are in server mode.");
			}
			VanillaPlayerController victim = (VanillaPlayerController) ((Server) Spout.getEngine()).getPlayer(args.getString(0), true).getController();
			if (victim != null) {
				victim.getHealth().die(source);
			}
		}
	}

	@Command(aliases = {"version", "vr"}, usage = "", desc = "Print out the version information for Vanilla", min = 0, max = 0)
	@CommandPermissions("vanilla.command.version")
	public void getVersion(CommandContext args, CommandSource source) {
		source.sendMessage("Vanilla ", plugin.getDescription().getVersion(), " (Implementing Minecraft protocol v", plugin.getDescription().getData("protocol").get(), ")");
		source.sendMessage("Powered by Spout " + Spout.getEngine().getVersion(), " (Implementing SpoutAPI ", Spout.getAPIVersion(), ")");
	}

	@Command(aliases = {"biome"}, usage = "", desc = "Print out the name of the biome at the current location", min = 0, max = 0)
	@CommandPermissions("vanilla.command.biome")
	public void getBiomeName(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only a player may call this command.");
		}
		Player player = (Player) source;
		if (!(player.getPosition().getWorld().getGenerator() instanceof BiomeGenerator)) {
			throw new CommandException("This map does not appear to have any biome data.");
		}
		Point pos = player.getPosition();
		Biome biome = pos.getWorld().getBiomeType(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
		source.sendMessage("Current biome: ", (biome != null ? biome.getName() : "none"));
	}
}
