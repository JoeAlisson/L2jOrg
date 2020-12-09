package org.l2j.gameserver.model.location;

import java.util.Objects;

/**
 * A datatype used to retain a 2D (x/y) point. It got the capability to be set and cleaned.
 */
public class Point2D
{
	protected volatile int _x;
	protected volatile int _y;
	
	public Point2D(int x, int y)
	{
		_x = x;
		_y = y;
	}
	
	@Override
	public Point2D clone()
	{
		return new Point2D(_x, _y);
	}
	
	@Override
	public String toString()
	{
		return _x + ", " + _y;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_x, _y);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final Point2D other = (Point2D) obj;
		return _x == other._x && _y == other._y;
	}
	
	/**
	 * @param x : The X coord to test.
	 * @param y : The Y coord to test.
	 * @return True if all coordinates equals this {@link Point2D} coordinates.
	 */
	public boolean equals(int x, int y)
	{
		return _x == x && _y == y;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public void setX(int x)
	{
		_x = x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public void setY(int y)
	{
		_y = y;
	}
	
	public void set(int x, int y)
	{
		_x = x;
		_y = y;
	}
	
	/**
	 * Refresh the current {@link Point2D} using a reference {@link Point2D} and a distance. The new destination is calculated to go in opposite side of the {@link Point2D} reference.<br>
	 * <br>
	 * This method is perfect to calculate fleeing characters position.
	 * @param referenceLoc : The Point2D used as position.
	 * @param distance : The distance to be set between current and new position.
	 */
	public void setFleeing(Point2D referenceLoc, int distance)
	{
		final double xDiff = referenceLoc.getX() - _x;
		final double yDiff = referenceLoc.getY() - _y;
		
		final double yxRation = Math.abs(xDiff / yDiff);
		
		final int y = (int) (distance / (yxRation + 1));
		final int x = (int) (y * yxRation);
		
		_x += (xDiff < 0 ? x : -x);
		_y += (yDiff < 0 ? y : -y);
	}
	
	public void clean()
	{
		_x = 0;
		_y = 0;
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @return The distance between this {@Point2D} and some given coordinates.
	 */
	public double distance2D(int x, int y)
	{
		final double dx = (double) _x - x;
		final double dy = (double) _y - y;
		
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	/**
	 * @param point : The {@link Point2D} to test.
	 * @return The distance between this {@Point2D} and the {@link Point2D} set as parameter.
	 */
	public double distance2D(Point2D point)
	{
		return distance2D(point.getX(), point.getY());
	}
	
	/**
	 * @param x : The X position to test.
	 * @param y : The Y position to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Point2D} is in the radius of some given coordinates.
	 */
	public boolean isIn2DRadius(int x, int y, int radius)
	{
		return distance2D(x, y) < radius;
	}
	
	/**
	 * @param point : The Point2D to test.
	 * @param radius : The radius to check.
	 * @return True if this {@link Point2D} is in the radius of the {@link Point2D} set as parameter.
	 */
	public boolean isIn2DRadius(Point2D point, int radius)
	{
		return distance2D(point) < radius;
	}
}