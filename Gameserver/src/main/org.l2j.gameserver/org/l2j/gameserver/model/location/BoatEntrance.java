package net.sf.l2j.gameserver.model.location;

public class BoatEntrance
{
	private Location _outerLocation;
	private Location _innerLocation;
	
	public BoatEntrance(Location outerLocation, Location innerLocation)
	{
		_outerLocation = outerLocation;
		_innerLocation = innerLocation;
	}
	
	public Location getOuterLocation()
	{
		return _outerLocation;
	}
	
	public Location getInnerLocation()
	{
		return _innerLocation;
	}
}