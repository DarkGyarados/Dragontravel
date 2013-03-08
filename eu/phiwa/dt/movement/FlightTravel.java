package eu.phiwa.dt.movement;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


import eu.phiwa.dt.DragonTravelMain;
import eu.phiwa.dt.XemDragon;
import eu.phiwa.dt.commands.CommandHandlers;
import eu.phiwa.dt.economy.EconomyHandler;
import eu.phiwa.dt.modules.MessagesLoader;
import eu.phiwa.dt.modules.Travels;

public class FlightTravel {

	public static void flyFlight(Flight flight, Player player, boolean takecost) {

		// Mounting the player
		if (!Travels.mountDragon(player))
			return;

		// Getting the dragon
		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);

		if (dragon == null)
			return;

		if (takecost) {
			if (!EconomyHandler.chargeFlightTravel(player))
				return;
		}

		CommandHandlers.dtCredit(player);
		player.sendMessage(MessagesLoader
				.replaceColors(DragonTravelMain.messages
						.getString("StartingFlight")));
		dragon.startFlight(flight);
	}

	public static void showFlights(Player player) {

		int amount = DragonTravelMain.wps.getIndices().size();
		StringBuilder a = new StringBuilder();

		if (amount == 0) {
			CommandHandlers.dtCredit(player);
			player.sendMessage(MessagesLoader
					.replaceColors(DragonTravelMain.messages
							.getString("NoFlightsYet")));
			return;
		}

		for (String index : DragonTravelMain.wps.getIndices()) {
			a.append(index + ", ");
		}

		CommandHandlers.dtCredit(player);
		player.sendMessage(MessagesLoader
				.replaceColors(DragonTravelMain.messages
						.getString("AvailableFlights")));
		player.sendMessage(ChatColor.RED + a.toString());
	}
}
