package org.l2j.gameserver.network.serverpackets.pledge;

import org.l2j.gameserver.enums.ClanRewardType;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerExPacketId;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author UnAfraid
 */
public class ExPledgeBonusUpdate extends ServerPacket {
    private final ClanRewardType _type;
    private final int _value;

    public ExPledgeBonusUpdate(ClanRewardType type, int value) {
        _type = type;
        _value = value;
    }

    @Override
    public void writeImpl(GameClient client) {
        writeId(ServerExPacketId.EX_PLEDGE_BONUS_UPDATE);
        writeByte(_type.getClientId());
        writeInt(_value);
    }

}
