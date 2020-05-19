package org.l2j.gameserver.network.clientpackets.pledgebonus;

import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ExPledgeClassicRaidInfo;
import org.l2j.gameserver.network.serverpackets.pledge.ExPledgeBonusOpen;

/**
 * @author UnAfraid
 */
public class RequestPledgeBonusOpen extends ClientPacket {
    @Override
    public void readImpl() {
    }

    @Override
    public void runImpl() {
        final Player player = client.getPlayer();
        if ((player == null) || (player.getClan() == null)) {
            return;
        }

        player.sendPacket(new ExPledgeBonusOpen(player));
        player.sendPacket(new ExPledgeClassicRaidInfo());
    }
}
