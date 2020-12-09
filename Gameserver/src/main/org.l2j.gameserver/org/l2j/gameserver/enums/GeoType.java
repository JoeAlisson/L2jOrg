package org.l2j.gameserver.enums;

public enum GeoType
{
	L2J("%d_%d.l2j"),
	L2OFF("%d_%d_conv.dat"),
	L2D("%d_%d.l2d");
	
	private final String _filename;
	
	private GeoType(String filename)
	{
		_filename = filename;
	}
	
	public String getFilename()
	{
		return _filename;
	}
}