package net.sf.l2j.commons.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

/**
 * This class, extending {@link HashMap}, is used to store pairs :
 * <ul>
 * <li>The key is a {@link String} ;</li>
 * <li>The value is any type of {@link Object}. Complex {@link Object}s such as {@link List}, {@link Map}, arrays or even {@link IntIntHolder} can also be stored.</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class StatSet extends HashMap<String, Object>
{
	public StatSet()
	{
		super();
	}
	
	public StatSet(final int size)
	{
		super(size);
	}
	
	public StatSet(final StatSet set)
	{
		super(set);
	}
	
	public void set(final String key, final Object value)
	{
		put(key, value);
	}
	
	public void set(final String key, final String value)
	{
		put(key, value);
	}
	
	public void set(final String key, final boolean value)
	{
		put(key, value ? Boolean.TRUE : Boolean.FALSE);
	}
	
	public void set(final String key, final int value)
	{
		put(key, value);
	}
	
	public void set(final String key, final int[] value)
	{
		put(key, value);
	}
	
	public void set(final String key, final long value)
	{
		put(key, value);
	}
	
	public void set(final String key, final double value)
	{
		put(key, value);
	}
	
	public void set(final String key, final Enum<?> value)
	{
		put(key, value);
	}
	
	public void unset(final String key)
	{
		remove(key);
	}
	
	public boolean getBool(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Boolean)
			return (Boolean) val;
		if (val instanceof String)
			return Boolean.parseBoolean((String) val);
		if (val instanceof Number)
			return ((Number) val).intValue() != 0;
		
		throw new IllegalArgumentException("StatSet : Boolean value required, but found: " + val + " for key: " + key + ".");
	}
	
	public boolean getBool(final String key, final boolean defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Boolean)
			return (Boolean) val;
		if (val instanceof String)
			return Boolean.parseBoolean((String) val);
		if (val instanceof Number)
			return ((Number) val).intValue() != 0;
		
		return defaultValue;
	}
	
	public byte getByte(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).byteValue();
		if (val instanceof String)
			return Byte.parseByte((String) val);
		
		throw new IllegalArgumentException("StatSet : Byte value required, but found: " + val + " for key: " + key + ".");
	}
	
	public byte getByte(final String key, final byte defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).byteValue();
		if (val instanceof String)
			return Byte.parseByte((String) val);
		
		return defaultValue;
	}
	
	public double getDouble(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).doubleValue();
		if (val instanceof String)
			return Double.parseDouble((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1. : 0.;
		
		throw new IllegalArgumentException("StatSet : Double value required, but found: " + val + " for key: " + key + ".");
	}
	
	public double getDouble(final String key, final double defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).doubleValue();
		if (val instanceof String)
			return Double.parseDouble((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1. : 0.;
		
		return defaultValue;
	}
	
	public double[] getDoubleArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof double[])
			return (double[]) val;
		if (val instanceof Number)
			return new double[]
			{
				((Number) val).doubleValue()
			};
		if (val instanceof String)
			return Stream.of(((String) val).split(";")).mapToDouble(Double::parseDouble).toArray();
		
		throw new IllegalArgumentException("StatSet : Double array required, but found: " + val + " for key: " + key + ".");
	}
	
	public float getFloat(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).floatValue();
		if (val instanceof String)
			return Float.parseFloat((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1 : 0;
		
		throw new IllegalArgumentException("StatSet : Float value required, but found: " + val + " for key: " + key + ".");
	}
	
	public float getFloat(final String key, final float defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).floatValue();
		if (val instanceof String)
			return Float.parseFloat((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1 : 0;
		
		return defaultValue;
	}
	
	public int getInteger(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).intValue();
		if (val instanceof String)
			return Integer.parseInt((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1 : 0;
		
		throw new IllegalArgumentException("StatSet : Integer value required, but found: " + val + " for key: " + key + ".");
	}
	
	public int getInteger(final String key, final int defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).intValue();
		if (val instanceof String)
			return Integer.parseInt((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1 : 0;
		
		return defaultValue;
	}
	
	public int[] getIntegerArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof int[])
			return (int[]) val;
		if (val instanceof Number)
			return new int[]
			{
				((Number) val).intValue()
			};
		if (val instanceof String)
			return Stream.of(((String) val).split(";")).mapToInt(Integer::parseInt).toArray();
		
		throw new IllegalArgumentException("StatSet : Integer array required, but found: " + val + " for key: " + key + ".");
	}
	
	public int[] getIntegerArray(final String key, final int[] defaultArray)
	{
		try
		{
			return getIntegerArray(key);
		}
		catch (IllegalArgumentException e)
		{
			return defaultArray;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(final String key)
	{
		final Object val = get(key);
		
		if (val == null)
			return Collections.emptyList();
		
		return (List<T>) val;
	}
	
	public long getLong(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).longValue();
		if (val instanceof String)
			return Long.parseLong((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1L : 0L;
		
		throw new IllegalArgumentException("StatSet : Long value required, but found: " + val + " for key: " + key + ".");
	}
	
	public long getLong(final String key, final long defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number)
			return ((Number) val).longValue();
		if (val instanceof String)
			return Long.parseLong((String) val);
		if (val instanceof Boolean)
			return (Boolean) val ? 1L : 0L;
		
		return defaultValue;
	}
	
	public long[] getLongArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof long[])
			return (long[]) val;
		if (val instanceof Number)
			return new long[]
			{
				((Number) val).longValue()
			};
		if (val instanceof String)
			return Stream.of(((String) val).split(";")).mapToLong(Long::parseLong).toArray();
		
		throw new IllegalArgumentException("StatSet : Long array required, but found: " + val + " for key: " + key + ".");
	}
	
	@SuppressWarnings("unchecked")
	public <T, U> Map<T, U> getMap(final String key)
	{
		final Object val = get(key);
		
		if (val == null)
			return Collections.emptyMap();
		
		return (Map<T, U>) val;
	}
	
	public String getString(final String key)
	{
		final Object val = get(key);
		
		if (val != null)
			return String.valueOf(val);
		
		throw new IllegalArgumentException("StatSet : String value required, but unspecified for key: " + key + ".");
	}
	
	public String getString(final String key, final String defaultValue)
	{
		final Object val = get(key);
		
		if (val != null)
			return String.valueOf(val);
		
		return defaultValue;
	}
	
	public String[] getStringArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[])
			return (String[]) val;
		if (val instanceof String)
			return ((String) val).split(";");
		
		throw new IllegalArgumentException("StatSet : String array required, but found: " + val + " for key: " + key + ".");
	}
	
	public IntIntHolder getIntIntHolder(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[])
		{
			final String[] toSplit = (String[]) val;
			return new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1]));
		}
		
		if (val instanceof String)
		{
			final String[] toSplit = ((String) val).split("-");
			return new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1]));
		}
		
		throw new IllegalArgumentException("StatSet : int-int (IntIntHolder) required, but found: " + val + " for key: " + key + ".");
	}
	
	public IntIntHolder[] getIntIntHolderArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[])
		{
			final String[] toSplit = (String[]) val;
			
			final IntIntHolder[] tempArray = new IntIntHolder[toSplit.length];
			
			int index = 0;
			for (String splitted : toSplit)
			{
				final String[] splittedHolder = splitted.split("-");
				tempArray[index++] = new IntIntHolder(Integer.parseInt(splittedHolder[0]), Integer.parseInt(splittedHolder[1]));
			}
			
			return tempArray;
		}
		
		if (val instanceof String)
		{
			// String exists, but it empty : return null.
			final String string = ((String) val);
			if (string.isEmpty())
				return null;
			
			// Single entry ; return the entry under array form.
			if (!string.contains(";"))
			{
				final String[] toSplit = string.split("-");
				final IntIntHolder[] tempArray = new IntIntHolder[1];
				tempArray[0] = new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1]));
				return tempArray;
			}
			
			final String[] toSplit = string.split(";");
			
			final IntIntHolder[] tempArray = new IntIntHolder[toSplit.length];
			
			int index = 0;
			for (String splitted : toSplit)
			{
				final String[] splittedHolder = splitted.split("-");
				tempArray[index++] = new IntIntHolder(Integer.parseInt(splittedHolder[0]), Integer.parseInt(splittedHolder[1]));
			}
			
			return tempArray;
		}
		
		throw new IllegalArgumentException("StatSet : int-int;int-int (int[] IntIntHolder) required, but found: " + val + " for key: " + key + ".");
	}
	
	public List<IntIntHolder> getIntIntHolderList(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String)
		{
			// String exists, but it empty : return a generic empty List.
			final String string = ((String) val);
			if (string.isEmpty())
				return Collections.emptyList();
			
			// Single entry ; return the entry under List form.
			if (!string.contains(";"))
			{
				final String[] toSplit = string.split("-");
				return Arrays.asList(new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1])));
			}
			
			// First split is using ";", second is using "-". Exemple : 1234-12;1234-12.
			final String[] entries = string.split(";");
			final List<IntIntHolder> list = new ArrayList<>(entries.length);
			
			// Feed the List.
			for (String entry : entries)
			{
				final String[] toSplit = entry.split("-");
				list.add(new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1])));
			}
			
			return list;
		}
		
		throw new IllegalArgumentException("StatSet : int-int;int-int (List<IntIntHolder>) required, but found: " + val + " for key: " + key + ".");
	}
	
	public List<IntIntHolder> getIntIntHolderList(final String key, final List<IntIntHolder> defaultHolder)
	{
		try
		{
			return getIntIntHolderList(key);
		}
		catch (IllegalArgumentException e)
		{
			return defaultHolder;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <A> A getObject(final String key, final Class<A> type)
	{
		final Object val = get(key);
		
		if (val == null || !type.isAssignableFrom(val.getClass()))
			return null;
		
		return (A) val;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass)
	{
		final Object val = get(name);
		
		if (val != null && enumClass.isInstance(val))
			return (E) val;
		if (val instanceof String)
			return Enum.valueOf(enumClass, (String) val);
		
		throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + "required, but found: " + val + ".");
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass, final E defaultValue)
	{
		final Object val = get(name);
		
		if (val != null && enumClass.isInstance(val))
			return (E) val;
		if (val instanceof String)
			return Enum.valueOf(enumClass, (String) val);
		
		return defaultValue;
	}
}