package org.l2j.gameserver.engine.geoengine.geodata;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.l2j.gameserver.enums.GeoType;

public class BlockMultilayer extends ABlock
{
	private static final int MAX_LAYERS = Byte.MAX_VALUE;
	
	private static ByteBuffer _temp;
	
	/**
	 * Initializes the temporary buffer.
	 */
	public static final void initialize()
	{
		// Initialize temporary buffer and sorting mechanism.
		_temp = ByteBuffer.allocate(GeoStructure.BLOCK_CELLS * MAX_LAYERS * 3);
		_temp.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	/**
	 * Releases temporary buffer.
	 */
	public static final void release()
	{
		_temp = null;
	}
	
	protected byte[] _buffer;
	
	/**
	 * Implicit constructor for children class.
	 */
	protected BlockMultilayer()
	{
		// Buffer is initialized in children class.
		_buffer = null;
	}
	
	/**
	 * Creates MultilayerBlock.
	 * @param bb : Input byte buffer.
	 * @param format : GeoFormat specifying format of loaded data.
	 */
	public BlockMultilayer(ByteBuffer bb, GeoType format)
	{
		// Move buffer pointer to end of MultilayerBlock.
		for (int cell = 0; cell < GeoStructure.BLOCK_CELLS; cell++)
		{
			// Get layer count for this cell.
			final byte layers = format != GeoType.L2OFF ? bb.get() : (byte) bb.getShort();
			
			if (layers <= 0 || layers > MAX_LAYERS)
				throw new RuntimeException("Invalid layer count for MultilayerBlock");
			
			// Add layers count.
			_temp.put(layers);
			
			// Loop over layers.
			for (byte layer = 0; layer < layers; layer++)
			{
				if (format != GeoType.L2D)
				{
					// Get data.
					short data = bb.getShort();
					
					// Add nswe and height.
					_temp.put((byte) (data & 0x000F));
					_temp.putShort((short) ((short) (data & 0xFFF0) >> 1));
				}
				else
				{
					// Add nswe.
					_temp.put(bb.get());
					
					// Add height.
					_temp.putShort(bb.getShort());
				}
			}
		}
		
		// Initialize buffer.
		_buffer = Arrays.copyOf(_temp.array(), _temp.position());
		
		// Clear temp buffer.
		_temp.clear();
	}
	
	@Override
	public final boolean hasGeoPos()
	{
		return true;
	}
	
	@Override
	public short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = getIndexNearest(geoX, geoY, worldZ, ignore);
		
		// Get height.
		return (short) (_buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = getIndexNearest(geoX, geoY, worldZ, ignore);
		
		// Get nswe.
		return _buffer[index];
	}
	
	@Override
	public int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += _buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to last layer data (first from bottom).
		byte layers = _buffer[index++];
		
		// Loop though all cell layers, find closest layer to given worldZ.
		int limit = Integer.MAX_VALUE;
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = _buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8;
			
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
	public int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += _buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to last layer data (first from bottom).
		byte layers = _buffer[index++];
		index += (layers - 1) * 3;
		
		// Loop though all layers, find first layer above worldZ.
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = _buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8;
			
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
	public int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Move index to the cell given by coordinates.
		int index = 0;
		for (int i = 0; i < (geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y); i++)
		{
			// Move index by amount of layers for this cell.
			index += _buffer[index] * 3 + 1;
		}
		
		// Get layers count and shift to first layer data (first from top).
		byte layers = _buffer[index++];
		
		// Loop though all layers, find first layer below worldZ.
		while (layers-- > 0)
		{
			// Get layer height.
			final int height = _buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8;
			
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
		// Get height.
		return (short) (_buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNswe(int index, IGeoObject ignore)
	{
		// Get nswe.
		return _buffer[index];
	}
	
	@Override
	public final void setNswe(int index, byte nswe)
	{
		// Set nswe.
		_buffer[index] = nswe;
	}
	
	@Override
	public final void saveBlock(BufferedOutputStream stream) throws IOException
	{
		// Write block type.
		stream.write(GeoStructure.TYPE_MULTILAYER_L2D);
		
		// For each cell.
		int index = 0;
		for (int i = 0; i < GeoStructure.BLOCK_CELLS; i++)
		{
			// Write layers count.
			byte layers = _buffer[index++];
			stream.write(layers);
			
			// Write cell's layer data.
			stream.write(_buffer, index, layers * 3);
			
			// Move index to next cell.
			index += layers * 3;
		}
	}
}