package org.l2j.gameserver.network.clientpackets;

import org.l2j.gameserver.data.xml.impl.LCoinShopData;
import org.l2j.gameserver.model.actor.instance.Player;
import org.l2j.gameserver.model.actor.request.LCoinShopRequest;
import org.l2j.gameserver.network.serverpackets.ExPurchaseLimitShopItemBuy;

public class RequestPurchaseLimitShopItemBuy extends ClientPacket {
    private int productId;
    private int amount;
    @Override
    protected void readImpl() throws Exception {
        readByte();
        productId = readInt();
        amount = readInt();
    }

    @Override
    protected void runImpl() {
        final Player player = client.getPlayer();
        if (player == null) {
            return;
        }

        var product = LCoinShopData.getInstance().getProductInfo(productId);
        var productItem = product.getProduction();

        if (player.hasItemRequest() || player.hasRequest(LCoinShopRequest.class) || player.getLCoins() < product.getIngredients().get(0).getCount()) {
            player.sendPacket(new ExPurchaseLimitShopItemBuy(LCoinShopData.getInstance().getProductInfo(productId), true));
            return;
        }

        player.addRequest(new LCoinShopRequest(player));
        player.addLCoins(-product.getIngredients().get(0).getCount());
        player.addItem("LCoinShop", productItem.getId(), productItem.getCount() * amount, player, true);
        player.removeRequest(LCoinShopRequest.class);
    }
}
