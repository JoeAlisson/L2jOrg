package net.sf.l2j.gameserver.model.location;

import java.util.ArrayList;
import java.util.List;

/**
 * A datatype extending {@link SpawnLocation}, which handles a single Control Tower spawn point and its parameters (such as guards npcId List), npcId to spawn and upgrade level.
 */
public class TowerSpawnLocation extends SpawnLocation
{
	private final int _npcId;
	private List<Integer> _zoneList;
	private int _upgradeLevel;
	
	public TowerSpawnLocation(int npcId, SpawnLocation location)
	{
		super(location);
		
		_npcId = npcId;
	}
	
	public TowerSpawnLocation(int npcId, SpawnLocation location, String[] zoneList)
	{
		super(location);
		
		_npcId = npcId;
		
		_zoneList = new ArrayList<>();
		for (String zoneId : zoneList)
			_zoneList.add(Integer.parseInt(zoneId));
	}
	
	public int getId()
	{
		return _npcId;
	}
	
	public List<Integer> getZoneList()
	{
		return _zoneList;
	}
	
	public void setUpgradeLevel(int level)
	{
		_upgradeLevel = level;
	}
	
	public int getUpgradeLevel()
	{
		return _upgradeLevel;
	}
}