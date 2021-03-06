package eu.phiwa.dt.modules;

import net.minecraft.server.v1_5_R2.World;

import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.entity.Player;

import eu.phiwa.dt.DragonTravelMain;
import eu.phiwa.dt.XemDragon;

/**
 * Handles all things related to stationary dragons,<br>
 * those dragons which are just on stations
 */
public class StationaryDragon {

	/**
	 * Creates a stationary dragon
	 */
	public static void createStatDragon(Player player) {
		World notchWorld = ((CraftWorld) player.getWorld()).getHandle();
		XemDragon XemDragon = new XemDragon(player.getLocation(), notchWorld);
		notchWorld.addEntity(XemDragon);
		player.sendMessage(MessagesLoader
				.replaceColors(DragonTravelMain.messages
						.getString("CreatedStationaryDragon")));
	}
}
