package org.l2j.gameserver.engine.geoengine.geodata;

public interface IBlockDynamic
{
	/**
	 * Adds {@link IGeoObject} to the {@link ABlock}. The block will update geodata according the object.
	 * @param object : {@link IGeoObject} to be added.
	 */
	public void addGeoObject(IGeoObject object);
	
	/**
	 * Removes {@link IGeoObject} from the {@link ABlock}. The block will update geodata according the object.
	 * @param object : {@link IGeoObject} to be removed.
	 */
	public void removeGeoObject(IGeoObject object);
}