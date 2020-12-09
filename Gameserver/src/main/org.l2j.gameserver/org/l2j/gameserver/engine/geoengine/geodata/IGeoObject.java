package org.l2j.gameserver.engine.geoengine.geodata;

public interface IGeoObject
{
	/**
	 * Returns geodata X coordinate of the {@link IGeoObject}.
	 * @return int : Geodata X coordinate.
	 */
	public int getGeoX();
	
	/**
	 * Returns geodata Y coordinate of the {@link IGeoObject}.
	 * @return int : Geodata Y coordinate.
	 */
	public int getGeoY();
	
	/**
	 * Returns geodata Z coordinate of the {@link IGeoObject}.
	 * @return int : Geodata Z coordinate.
	 */
	public int getGeoZ();
	
	/**
	 * Returns height of the {@link IGeoObject}.
	 * @return int : Height.
	 */
	public int getHeight();
	
	/**
	 * Returns {@link IGeoObject} data.
	 * @return byte[][] : {@link IGeoObject} data.
	 */
	public byte[][] getObjectGeoData();
}
