package org.l2j.gameserver.engine.geoengine.geodata;

import java.util.LinkedList;
import java.util.List;

public final class BlockMultilayerDynamic extends BlockMultilayer implements IBlockDynamic
{
	private final int _bx;
	private final int _by;
	private final byte[] _original;
	private final List<IGeoObject> _objects;
	
	/**
	 * Creates {@link BlockMultilayerDynamic}.
	 * @param bx : Block X coordinate.
	 * @param by : Block Y coordinate.
	 * @param block : The original MultilayerBlock to create a dynamic version from.
	 */
	public BlockMultilayerDynamic(int bx, int by, BlockMultilayer block)
	{
		// Move buffer from ComplexBlock object to this object.
		_buffer = block._buffer;
		block._buffer = null;
		
		// Get block coordinates.
		_bx = bx;
		_by = by;
		
		// Create copy for dynamic implementation.
		_original = new byte[_buffer.length];
		System.arraycopy(_buffer, 0, _original, 0, _buffer.length);
		
		// Create list for geo objects.
		_objects = new LinkedList<>();
	}
	
	@Override
	public short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Get cell index.
		final int index = getIndexNearest(geoX, geoY, worldZ, ignore);
		
		// Get height.
		return (short) (buffer[index + 1] & 0x00FF | buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Get cell index.
		final int index = getIndexNearest(geoX, geoY, worldZ, ignore);
		
		// Get nswe.
		return buffer[index];
	}
	
	@Override
	public final int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to last layer data (first from bottom).
		byte layers = buffer[index++];
		
		// Loop though all cell layers, find closest layer to given worldZ.
		int limit = Integer.MAX_VALUE;
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = buffer[index + 1] & 0x00FF | buffer[index + 2] << 8;
			
			// Get Z distance and compare with limit.
			// Note: When 2 layers have same distance to worldZ (worldZ is in the middle of them):
			// > Returns bottom layer.
			// >= Returns upper layer.
			final int distance = Math.abs(height - worldZ);
			if (distance > limit)
				break;
			
			// Update limit and move to next layer.
			limit = distance;
			index += 3;
		}
		
		// Return layer index.
		return index - 3;
	}
	
	@Override
	public final int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to last layer data (first from bottom).
		byte layers = buffer[index++];
		index += (layers - 1) * 3;
		
		// Loop though all layers, find first layer above worldZ.
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = buffer[index + 1] & 0x00FF | buffer[index + 2] << 8;
			
			// Layer height is higher than worldZ, return layer index.
			if (height > worldZ)
				return index;
			
			// Move index to next layer.
			index -= 3;
		}
		
		// No layer found.
		return -1;
	}
	
	@Override
	public final int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to first layer data (first from top).
		byte layers = buffer[index++];
		
		// Loop though all layers, find first layer below worldZ.
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = buffer[index + 1] & 0x00FF | buffer[index + 2] << 8;
			
			// Layer height is lower than worldZ, return layer index.
			if (height < worldZ)
				return index;
			
			// Move index to next layer.
			index += 3;
		}
		
		// No layer found.
		return -1;
	}
	
	@Override
	public short getHeight(int index, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Get height.
		return (short) (buffer[index + 1] & 0x00FF | buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNswe(int index, IGeoObject ignore)
	{
		// Get geodata buffer based on given IGeoObject.
		byte buffer[] = _objects.contains(ignore) ? _original : _buffer;
		
		// Get nswe.
		return buffer[index];
	}
	
	@Override
	public synchronized final void addGeoObject(IGeoObject object)
	{
		// Add geo object, update block geodata when added.
		if (_objects.add(object))
			update();
	}
	
	@Override
	public synchronized final void removeGeoObject(IGeoObject object)
	{
		// Remove geo object, update block geodata when removed.
		if (_objects.remove(object))
			update();
	}
	
	/**
	 * Resets current geodata to original state and than apply all {@link IGeoObject}'s modifications.
	 */
	private final void update()
	{
		// Reset current geodata.
		System.arraycopy(_original, 0, _buffer, 0, _original.length);
		
		// Get block geo coordinates.
		final int minBX = _bx * GeoStructure.BLOCK_CELLS_X;
		final int minBY = _by * GeoStructure.BLOCK_CELLS_Y;
		final int maxBX = minBX + GeoStructure.BLOCK_CELLS_X;
		final int maxBY = minBY + GeoStructure.BLOCK_CELLS_Y;
		
		// For all objects.
		for (IGeoObject object : _objects)
		{
			// Get object geo coordinates and other object variables.
			final int minOX = object.getGeoX();
			final int minOY = object.getGeoY();
			final int minOZ = object.getGeoZ();
			final int maxOZ = minOZ + object.getHeight();
			final byte[][] geoData = object.getObjectGeoData();
			
			// Calculate min/max geo coordinates for iteration (intersection of block and object).
			final int minGX = Math.max(minBX, minOX);
			final int minGY = Math.max(minBY, minOY);
			final int maxGX = Math.min(maxBX, minOX + geoData.length);
			final int maxGY = Math.min(maxBY, minOY + geoData[0].length);
			
			// Iterate over intersection of block and object.
			for (int gx = minGX; gx < maxGX; gx++)
			{
				for (int gy = minGY; gy < maxGY; gy++)
				{
					// Get object nswe.
					final byte objNswe = geoData[gx - minOX][gy - minOY];
					
					// Object contains no change of data in this cell, continue to next cell.
					if (objNswe == 0xFF)
						continue;
					
					// Get block index of this cell.
					int ib = getIndexNearest(gx, gy, minOZ, null);
					
					// Compare block data and original data, when height differs:
					// -> height was affected by other geo object
					// -> cell is inside an object
					// -> no need to check/change it anymore (Z is lifted, nswe is 0).
					// Compare is done in raw format (2 bytes) instead of conversion to short.
					if (_buffer[ib + 1] != _original[ib + 1] || _buffer[ib + 2] != _original[ib + 2])
						continue;
					
					// So far cell is not inside of any object.
					if (objNswe == 0)
					{
						// Cell is inside of this object -> set nswe to 0 and lift Z up.
						
						// Set block nswe.
						_buffer[ib] = 0;
						
						// Calculate object height, limit to next layer.
						int z = maxOZ;
						int i = getIndexAbove(gx, gy, minOZ, null);
						if (i != -1)
						{
							int az = getHeight(i, null);
							if (az <= maxOZ)
								z = az - GeoStructure.CELL_IGNORE_HEIGHT;
						}
						
						// Set block Z to object height.
						_buffer[ib + 1] = (byte) (z & 0x00FF);
						_buffer[ib + 2] = (byte) (z >> 8);
					}
					else
					{
						// Cell is outside of this object -> update nswe.
						
						// Height different is too high (trying to update another layer), skip.
						short z = getHeight(ib, null);
						if (Math.abs(z - minOZ) > GeoStructure.CELL_IGNORE_HEIGHT)
							continue;
						
						// Adjust block nswe according to the object nswe.
						_buffer[ib] &= objNswe;
					}
				}
			}
		}
	}
}