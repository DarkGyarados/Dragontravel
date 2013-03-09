package eu.phiwa.dt.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import eu.phiwa.dt.DragonTravelMain;
import eu.phiwa.dt.commands.CommandHandlers;
import eu.phiwa.dt.economy.EconomyHandler;
import eu.phiwa.dt.modules.MessagesLoader;
import eu.phiwa.dt.movement.Flight;
import eu.phiwa.dt.movement.FlightTravel;

/**
 * Copyright (C) 2011-2013 Philipp Wagner
 * mail@phiwa.eu
 * 
 * Credits for one year of development go to Luca Moser (moser.luca@gmail.com/)
 * 
 * This file is part of the Bukkit-plugin DragonTravel.
 * 
 * DragonTravel is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * DragonTravel is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with this project.
 * If not, see <http://www.gnu.org/licenses/>.
 */

public class FlightSignsInteract implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onDestinationSignInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Sign sign = null;

		if (block == null)
			return;

		if (block.getTypeId() == 63 || block.getTypeId() == 68) {

			sign = (Sign) block.getState();

			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)
					|| event.getAction().equals(Action.LEFT_CLICK_AIR)) {
				return;
			}

			if (!sign.getLine(0).equalsIgnoreCase(
					ChatColor.GOLD + "DragonTravel")) {
				return;
			}

			if (!sign.getLine(1).equalsIgnoreCase("Flight")) {
				return;
			}

			// checkin if player has permission to use flight signs
			if (!player.hasPermission("dt.flightsigns.use")) {
				CommandHandlers.noPerm(player);
				return;
			}

			// Checking if the flight still exists
			if (!Flight.existFlight(sign.getLine(2))) {
				CommandHandlers.dtCredit(player);
				player.sendMessage(MessagesLoader
						.replaceColors(DragonTravelMain.messages
								.getString("FlightDoesNotExist")));
				return;
			}

			// Here comes the cost withdraw, player won't fly if
			// the withdraw didnt occur successfully
			if (!sign.getLine(3).isEmpty()) {

				String[] split = sign.getLine(3).split(":");
				double cost = Double.parseDouble(split[1].trim());

				if (!player.hasPermission("dt.nocost"))
					if (!EconomyHandler.chargePlayerSigns(player, cost))
						return;
			}

			// Start the flight
			Flight flight = new Flight(sign.getLine(2));
			FlightTravel.flyFlight(flight, player, false);
		}
	}

}
