package net.sf.l2j.gameserver.model.location;

import java.util.Objects;

public class RadarMarker extends Location
{
	private int _type;
	
	public RadarMarker(int type, int x, int y, int z)
	{
		super(x, y, z);
		
		_type = type;
	}
	
	public RadarMarker(int x, int y, int z)
	{
		super(x, y, z);
		
		_type = 1;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(_type);
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
		
		final RadarMarker other = (RadarMarker) obj;
		return _type == other._type;
	}
}