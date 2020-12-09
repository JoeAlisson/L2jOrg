package net.sf.l2j.gameserver.model.location;

/**
 * A datatype extending {@link Location} used for boats. It notably holds move speed and rotation speed.
 */
public class BoatLocation extends Location
{
	private int _moveSpeed;
	private int _rotationSpeed;
	
	public BoatLocation(int x, int y, int z)
	{
		super(x, y, z);
		
		_moveSpeed = 350;
		_rotationSpeed = 4000;
	}
	
	public BoatLocation(int x, int y, int z, int moveSpeed, int rotationSpeed)
	{
		super(x, y, z);
		
		_moveSpeed = moveSpeed;
		_rotationSpeed = rotationSpeed;
	}
	
	public int getMoveSpeed()
	{
		return _moveSpeed;
	}
	
	public int getRotationSpeed()
	{
		return _rotationSpeed;
	}
}