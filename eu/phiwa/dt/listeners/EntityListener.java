package eu.phiwa.dt.listeners;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import eu.phiwa.dt.DragonTravelMain;
import eu.phiwa.dt.XemDragon;

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
public class EntityListener implements Listener {

	DragonTravelMain plugin;

	public EntityListener(DragonTravelMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnderDragonExlplode(EntityExplodeEvent event) {

		if (DragonTravelMain.onlydragontraveldragons
				&& event.getEntity() instanceof XemDragon)
			event.setCancelled(true);

		else if (DragonTravelMain.alldragons
				&& event.getEntity() instanceof EnderDragon)
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		if (DragonTravelMain.TravelInformation.containsKey(player))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(EntityDeathEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (!DragonTravelMain.TravelInformation.containsKey(player))
			return;

		XemDragon dragon = DragonTravelMain.TravelInformation.get(player);
		Entity dragona = (Entity) dragon.getEntity();
		dragona.remove();
		DragonTravelMain.TravelInformation.remove(player);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {

		if (!event.getEntity().getType().toString().equals("ENDER_DRAGON"))
			return;

		if (!event.isCancelled())
			return;

		if (DragonTravelMain.ignoreAntiMobspawnAreas == true)
			event.setCancelled(false);
	}
}
