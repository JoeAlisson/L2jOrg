package net.sf.l2j.gameserver.model.location;

/**
 * A datatype used as teleportation point reminder. Used by GM admincommand //bk.
 */
public class Bookmark extends Location
{
	private final String _name;
	private final int _objId;
	
	public Bookmark(String name, int objId, int x, int y, int z)
	{
		super(x, y, z);
		
		_name = name;
		_objId = objId;
		_x = x;
		_y = y;
		_z = z;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getId()
	{
		return _objId;
	}
}