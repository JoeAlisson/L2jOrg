package net.sf.l2j.gameserver.model.location;

import java.util.Calendar;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.manager.SevenSignsManager;
import net.sf.l2j.gameserver.enums.SealType;
import net.sf.l2j.gameserver.enums.TeleportType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;

/**
 * A datatype extending {@link Location}, used to retain a single Gatekeeper teleport location.
 */
public class Teleport extends Location
{
	private final String _desc;
	private final TeleportType _type;
	private final int _priceId;
	private final int _priceCount;
	private final int _castleId;
	
	public Teleport(StatSet set)
	{
		super(set.getInteger("x"), set.getInteger("y"), set.getInteger("z"));
		
		_desc = set.getString("desc");
		_type = set.getEnum("type", TeleportType.class, TeleportType.STANDARD);
		_priceId = set.getInteger("priceId");
		_priceCount = set.getInteger("priceCount");
		_castleId = set.getInteger("castleId", 0);
	}
	
	@Override
	public String toString()
	{
		return "TeleportLocation [_desc=" + _desc + ", _type=" + _type + ", _priceId=" + _priceId + ", _priceCount=" + _priceCount + ", _castleId=" + _castleId + "]";
	}
	
	public String getDesc()
	{
		return _desc;
	}
	
	public TeleportType getType()
	{
		return _type;
	}
	
	public int getPriceId()
	{
		return _priceId;
	}
	
	public int getPriceCount()
	{
		return _priceCount;
	}
	
	public int getCastleId()
	{
		return _castleId;
	}
	
	/**
	 * In L2OFF half price teleport feature is set in 'event.ini' and is named 'CoreTime'.<br>
	 * However some custom/extended L2OFF packs are likely to use type "PRIMEHOURS", but it is nothing more than static data duplication.<br>
	 * Also core-time shall effect only standard teleport.
	 * @return True if the time is core time or not.
	 */
	private static boolean isCoreTime()
	{
		final Calendar now = Calendar.getInstance();
		switch (now.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				final int currentHour = now.get(Calendar.HOUR_OF_DAY);
				return currentHour >= 20 && currentHour <= 23;
		}
		
		return false;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return The teleport price, modified by multiple sources (Seven Signs, half price time).
	 */
	public int getCalculatedPriceCount(Player player)
	{
		if (_priceId == PcInventory.ANCIENT_ADENA_ID)
		{
			final SevenSignsManager ss = SevenSignsManager.getInstance();
			final boolean check = ss.isSealValidationPeriod() && ss.getPlayerCabal(player.getObjectId()) == ss.getSealOwner(SealType.GNOSIS) && ss.getPlayerSeal(player.getObjectId()) == SealType.GNOSIS;
			
			return (check) ? _priceCount : (int) (_priceCount * 1.6);
		}
		
		// Half price system.
		if (_type == TeleportType.STANDARD && isCoreTime())
			return Math.max(_priceCount >> 1, 1);
		
		return _priceCount;
	}
}