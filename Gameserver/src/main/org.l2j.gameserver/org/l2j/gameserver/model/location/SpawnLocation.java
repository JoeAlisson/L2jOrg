package org.l2j.gameserver.model.location;

import org.l2j.gameserver.model.WorldObject;
import org.l2j.gameserver.util.MathUtil;

import java.util.Objects;

/**
 * A datatype extending {@link Location}, wildly used as character position, since it also stores heading of the character.
 */
public class SpawnLocation extends Location
{
	public static final SpawnLocation DUMMY_SPAWNLOC = new SpawnLocation(0, 0, 0, 0);
	
	protected volatile int _heading;
	
	public SpawnLocation(int x, int y, int z, int heading)
	{
		super(x, y, z);
		
		_heading = heading;
	}
	
	public SpawnLocation(SpawnLocation loc)
	{
		super(loc.getX(), loc.getY(), loc.getZ());
		
		_heading = loc.getHeading();
	}
	
	@Override
	public SpawnLocation clone()
	{
		return new SpawnLocation(_x, _y, _z, _heading);
	}
	
	@Override
	public String toString()
	{
		return _x + ", " + _y + ", " + _z + ", " + _heading;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(_heading);
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (!super.equals(obj))
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final SpawnLocation other = (SpawnLocation) obj;
		return _heading == other._heading;
	}
	
	@Override
	public void clean()
	{
		super.set(0, 0, 0);
		
		_heading = 0;
	}
	
	public int getHeading()
	{
		return _heading;
	}
	
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	
	/**
	 * Set the heading of this {@link SpawnLocation} to face a 2D point.
	 * @param targetX : The X target to face.
	 * @param targetY : The Y target to face.
	 */
	public void setHeadingTo(int targetX, int targetY)
	{
		_heading = MathUtil.calculateHeadingFrom(_x, _y, targetX, targetY);
	}
	
	/**
	 * Set the heading of this {@link SpawnLocation} to face a {@link WorldObject}.
	 * @param object : The WorldObject to face.
	 * @see #setHeadingTo(int, int)
	 */
	public void setHeadingTo(WorldObject object)
	{
		setHeadingTo(object.getX(), object.getY());
	}
	
	/**
	 * Set the heading of this {@link SpawnLocation} to face a {@link Location}.
	 * @param loc : The Location to face.
	 * @see #setHeadingTo(int, int)
	 */
	public void setHeadingTo(Location loc)
	{
		setHeadingTo(loc.getX(), loc.getY());
	}
	
	public void set(int x, int y, int z, int heading)
	{
		super.set(x, y, z);
		
		_heading = heading;
	}
	
	public void set(SpawnLocation loc)
	{
		super.set(loc.getX(), loc.getY(), loc.getZ());
		
		_heading = loc.getHeading();
	}
	
	/**
	 * Add an offset to this {@link SpawnLocation} based on current heading.
	 * @param offset : The offset to add.
	 */
	public void addOffsetBasedOnHeading(int offset)
	{
		final double radian = Math.toRadians(MathUtil.convertHeadingToDegree(_heading));
		
		_x += (int) (Math.cos(radian) * offset);
		_y += (int) (Math.sin(radian) * offset);
	}
	
	/**
	 * @param target : The {@link WorldObject} target to check.
	 * @return True if this {@link SpawnLocation} is behind the {@link WorldObject} target.
	 */
	public boolean isBehind(WorldObject target)
	{
		if (target == null)
			return false;
		
		final double maxAngleDiff = 60;
		final double angleChar = MathUtil.calculateAngleFrom(_x, _y, target.getX(), target.getY());
		final double angleTarget = MathUtil.convertHeadingToDegree(target.getHeading());
		
		double angleDiff = angleChar - angleTarget;
		
		if (angleDiff <= -360 + maxAngleDiff)
			angleDiff += 360;
		
		if (angleDiff >= 360 - maxAngleDiff)
			angleDiff -= 360;
		
		return Math.abs(angleDiff) <= maxAngleDiff;
	}
	
	/**
	 * @param target : The {@link WorldObject} target to check.
	 * @return True if this {@link SpawnLocation} is in front of the {@link WorldObject} target.
	 */
	public boolean isInFrontOf(WorldObject target)
	{
		if (target == null)
			return false;
		
		final double maxAngleDiff = 60;
		final double angleTarget = MathUtil.calculateAngleFrom(target.getX(), target.getY(), _x, _y);
		final double angleChar = MathUtil.convertHeadingToDegree(target.getHeading());
		
		double angleDiff = angleChar - angleTarget;
		
		if (angleDiff <= -360 + maxAngleDiff)
			angleDiff += 360;
		
		if (angleDiff >= 360 - maxAngleDiff)
			angleDiff -= 360;
		
		return Math.abs(angleDiff) <= maxAngleDiff;
	}
	
	/**
	 * @param target : The {@link WorldObject} target to check.
	 * @param maxAngle : The angle to check.
	 * @return True if this {@link SpawnLocation} is facing the {@link WorldObject} target.
	 */
	public boolean isFacing(WorldObject target, int maxAngle)
	{
		if (target == null)
			return false;
		
		final double maxAngleDiff = maxAngle / 2;
		final double angleTarget = MathUtil.calculateAngleFrom(_x, _y, target.getX(), target.getY());
		final double angleChar = MathUtil.convertHeadingToDegree(getHeading());
		
		double angleDiff = angleChar - angleTarget;
		
		if (angleDiff <= -360 + maxAngleDiff)
			angleDiff += 360;
		
		if (angleDiff >= 360 - maxAngleDiff)
			angleDiff -= 360;
		
		return Math.abs(angleDiff) <= maxAngleDiff;
	}
}