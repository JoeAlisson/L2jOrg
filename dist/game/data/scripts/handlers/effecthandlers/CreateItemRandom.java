/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import java.util.logging.Logger;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.mobius.gameserver.model.StatsSet;
import org.l2j.gameserver.mobius.gameserver.model.actor.L2Character;
import org.l2j.gameserver.mobius.gameserver.model.actor.instance.L2PcInstance;
import org.l2j.gameserver.mobius.gameserver.model.effects.AbstractEffect;
import org.l2j.gameserver.mobius.gameserver.model.holders.ItemChanceHolder;
import org.l2j.gameserver.mobius.gameserver.model.items.instance.L2ItemInstance;
import org.l2j.gameserver.mobius.gameserver.model.skills.Skill;
import org.l2j.gameserver.mobius.gameserver.network.SystemMessageId;

/**
 * @author UnAfraid
 */
public class CreateItemRandom extends AbstractEffect
{
	private static final Logger LOGGER = Logger.getLogger(CreateItemRandom.class.getName());
	
	public CreateItemRandom(StatsSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return Boolean.TRUE;
	}
	
	@Override
	public void instant(L2Character effector, L2Character effected, Skill skill, L2ItemInstance item)
	{
		final L2PcInstance player = effected.getActingPlayer();
		if (player == null)
		{
			return;
		}
		else if (item == null)
		{
			LOGGER.warning("" + player + " Attempting to cast skill: " + skill + " without item defined!");
			return;
		}
		else if (item.getItem().getCreateItems().isEmpty())
		{
			LOGGER.warning("" + player + " Attempting to cast skill: " + skill + " with item " + item + " without createItems defined!");
			return;
		}
		
		double cumulativeChance = 0;
		final int random = Rnd.get(100);
		for (ItemChanceHolder holder : item.getItem().getCreateItems())
		{
			cumulativeChance += holder.getChance();
			if (random < cumulativeChance)
			{
				player.addItem("CreateItems", holder.getId(), holder.getCount(), player, true);
				return;
			}
		}
		
		player.sendPacket(SystemMessageId.THERE_WAS_NOTHING_FOUND_INSIDE);
	}
}