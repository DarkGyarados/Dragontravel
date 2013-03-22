package eu.phiwa.dt;


import eu.phiwa.dt.modules.Travels;
import eu.phiwa.dt.movement.Flight;
import eu.phiwa.dt.movement.Waypoint;
import net.minecraft.server.v1_5_R2.EntityEnderDragon;
import net.minecraft.server.v1_5_R2.World;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

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
public class XemDragon extends EntityEnderDragon {

	// Travel
	private double toX;
	private double toY;
	private double toZ;
	private int maxY;
	private boolean finalmove = false;
	private boolean move = false;

	// Flight
	private Flight flight;
	private Waypoint firstwp;

	// First Waypoint coords
	private double fwpX;
	private double fwpY;
	private double fwpZ;

	// Amount to fly up/down during a flight
	private double XTick;
	private double YTick;
	private double ZTick;

	// Distance to the right wp coords
	private double distanceX;
	private double distanceY;
	private double distanceZ;

	// Start points for tick calculation
	private double startX;
	private double startY;
	private double startZ;

	// Basics
	boolean isFlight = false;
	boolean isTravel = false;
	Entity entity;

	// Start Location
	Location start;

	public XemDragon(Location loca, World notchWorld) {

		super(notchWorld);

		this.start = loca;
		setPosition(loca.getX(), loca.getY(), loca.getZ());
		yaw = loca.getYaw() + 180;
		while (yaw > 360)
			yaw -= 360;
		while (yaw < 0)
			yaw += 360;
		if (yaw < 45 || yaw > 315)
			yaw = 0F;
		else if (yaw < 135)
			yaw = 90F;
		else if (yaw < 225)
			yaw = 180F;
		else
			yaw = 270F;
	}

	public XemDragon(World world) {
		super(world);
	}

	public Entity getEntity() {
		if (bukkitEntity != null)
			return bukkitEntity;
		else
			return entity;
	
	}

	public void startTravel(Location loc) {

		toX = loc.getBlockX();
		toY = loc.getBlockY();
		toZ = loc.getBlockZ();

		this.startX = start.getX();
		this.startY = start.getY();
		this.startZ = start.getZ();

		maxY = DragonTravelMain.config.getInt("TravelHeight");

		setMoveTravel();
		yaw = getCorrectYaw(toX, toZ);
		isTravel = true;
		move = true;
	}

	public void startFlight(Flight flight) {

		entity = getEntity();

		this.flight = flight;
		this.flight.loadWPs();

		this.firstwp = flight.getFirstWaypoint();
		this.fwpX = firstwp.getX();
		this.fwpY = firstwp.getY();
		this.fwpZ = firstwp.getZ();

		this.startX = start.getX();
		this.startY = start.getY();
		this.startZ = start.getZ();

		toX = fwpX;
		toY = fwpY;
		toZ = fwpZ;

		setMoveFlight();
		yaw = getCorrectYaw(toX, toZ);
		move = true;
		isFlight = true;
	}

	/**
	 * Gets the correct yaw for this specific path
	 */
	private float getCorrectYaw(double targetx, double targetz) {
		if (this.locZ > targetz)
			return (float) (-Math.toDegrees(Math.atan((this.locX - targetx)	/ (this.locZ - targetz))));
		if (this.locZ < targetz)
			return (float) (-Math.toDegrees(Math.atan((this.locX - targetx)	/ (this.locZ - targetz)))) + 180.0F;
		return this.yaw;
	}

	/**
	 * Sets the x,y,z move for each tick
	 */
	public void setMoveFlight() {

		this.distanceX = this.startX - toX;
		this.distanceY = this.startY - toY;
		this.distanceZ = this.startZ - toZ;

		double tick = Math.sqrt((distanceX * distanceX)	+ (distanceY * distanceY)
				+ (distanceZ * distanceZ)) / DragonTravelMain.speed;
		YTick = Math.abs(distanceY) / tick;
		XTick = Math.abs(distanceX) / tick;
		ZTick = Math.abs(distanceZ) / tick;
	}

	/**
	 * Sets the x,z move for each tick
	 */
	public void setMoveTravel() {

		this.distanceX = this.startX - toX;
		this.distanceY = this.startY - toY;
		this.distanceZ = this.startZ - toZ;

		double tick = Math.sqrt((distanceX * distanceX)
						+ (distanceY * distanceY)
						+ (distanceZ * distanceZ))
						/ DragonTravelMain.speed;
		XTick = Math.abs(distanceX) / tick;
		ZTick = Math.abs(distanceZ) / tick;
	}

	@Override
	public void c() {

		// Travel
		if (isTravel) {
			travel();
			return;
		}

		// Flight
		if (isFlight) {
			flight();
		}
	}

	/**
	 * Flight with waypoints
	 */
	public void flight() {

		// Returns, the dragon won't move
		if (!move)
			return;

		// Init move variables
		double myX = locX;
		double myY = locY;
		double myZ = locZ;

		if ((int) myX != (int) toX) {
			if (myX < toX)
				myX += XTick;
			else 
				myX -= XTick;
		}

		if ((int) myY != (int) toY) {
			if (myY < toY)
				myY += YTick;
			else
				myY -= YTick;
		}

		if ((int) myZ != (int) toZ) {
			if (myZ < toZ)
				myZ += ZTick;
			else
				myZ -= ZTick;
		}

		// If myZ = toZ, then we will load the next waypoint or
		// finish the flight, in case it was the last waypoint to fly
		if ((int) myZ == (int) toZ && (int) myY == (int) toY
				&& (int) myX == (int) toX) {
			Waypoint wp = flight.getNextWaypoint();

			// Removing the entity and dismouting the player
			if (wp == null) {
				Travels.removePlayerandDragon(entity);
				return;
			}

			this.startX = locX;
			this.startY = locY;
			this.startZ = locZ;

			toX = wp.getX();
			toY = wp.getY();
			toZ = wp.getZ();
			setMoveFlight();
			yaw = getCorrectYaw(toX, toZ);
			return;
		}

		setPosition(myX, myY, myZ);
	}

	/**
	 * Normal Travel
	 */
	public void travel() {

		// Returns, the dragon won't move
		if (!move)
			return;

		entity = getEntity();

		if (entity.getPassenger() == null)
			return;

		double myX = locX;
		double myY = locY;
		double myZ = locZ;

		if (finalmove) {

			// Flying down on end
			if ((int) locY > (int) toY)
				myY -= DragonTravelMain.speed;

			// Flying up on end
			else if ((int) locY < (int) toY)
				myY += DragonTravelMain.speed;

			// Removing entity
			else {
				Travels.removePlayerandDragon(entity);
				return;
			}

			setPosition(myX, myY, myZ);
			return;
		}

		// Getting the correct height
		if ((int) locY < maxY)
			myY += DragonTravelMain.speed;

		if (myX < toX)
			myX += XTick;
		else
			myX -= XTick;

		if (myZ < toZ)
			myZ += ZTick;
		else
			myZ -= ZTick;

		if ((int) myZ == (int) toZ
		  && ((int)myX == (int)toX
		   || (int)myX == (int)toX+1
		   || (int)myX == (int)toX-1)	  	
		  )
			finalmove = true;

		setPosition(myX, myY, myZ);
	}

	public double x_() {
		return 3;
	}
}