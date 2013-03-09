package eu.phiwa.dt.listeners;


import eu.phiwa.dt.DragonTravelMain;
import eu.phiwa.dt.modules.MessagesLoader;
import eu.phiwa.dt.spout.gui.MenuScreen;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.player.SpoutPlayer;

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
public class InputListener implements Listener {

	public DragonTravelMain plugin;

	@EventHandler
	public void onKeyPress(KeyPressedEvent event) {

		SpoutPlayer player = event.getPlayer();

		if (!player.isSpoutCraftEnabled())
			return;

		if (event.getScreenType() != ScreenType.GAME_SCREEN)
			return;

		String keypressed = event.getKey().name();

		if (!keypressed.equalsIgnoreCase(DragonTravelMain.config
				.getString("GUIopenKey")))
			return;

		if (!player.hasPermission("dt.gui")) {
			player.sendMessage(MessagesLoader
					.replaceColors(DragonTravelMain.messages
							.getString("NoPermission")));
			return;
		}

		MenuScreen pop = new MenuScreen(player);
		pop.createGUI(player);
	}

}
