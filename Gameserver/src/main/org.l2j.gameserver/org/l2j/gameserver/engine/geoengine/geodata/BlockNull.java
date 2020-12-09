package org.l2j.gameserver.engine.geoengine.geodata;

import java.io.BufferedOutputStream;

import org.l2j.gameserver.enums.GeoType;

public class BlockNull extends ABlock
{
	private byte _nswe;
	
	public BlockNull(GeoType format)
	{
		// Get nswe.
		_nswe = format != GeoType.L2D ? 0x0F : (byte) (0xFF);
	}
	
	@Override
	public final boolean hasGeoPos()
	{
		return false;
	}
	
	@Override
	public final short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return (short) worldZ;
	}
	
	@Override
	public final byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return _nswe;
	}
	
	@Override
	public final int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final short getHeight(int index, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final byte getNswe(int index, IGeoObject ignore)
	{
		return _nswe;
	}
	
	@Override
	public final void setNswe(int index, byte nswe)
	{
	}
	
	@Override
	public final void saveBlock(BufferedOutputStream stream)
	{
	}
}