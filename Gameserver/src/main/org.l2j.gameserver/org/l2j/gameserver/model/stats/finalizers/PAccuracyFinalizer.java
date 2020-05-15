package org.l2j.gameserver.model.stats.finalizers;

import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.item.BodyPart;
import org.l2j.gameserver.model.stats.IStatsFunction;
import org.l2j.gameserver.model.stats.Stat;
import org.l2j.gameserver.world.WorldTimeController;

import java.util.Optional;

import static org.l2j.gameserver.util.GameUtils.isPlayer;

/**
 * @author UnAfraid
 */
public class PAccuracyFinalizer implements IStatsFunction {
    @Override
    public double calc(Creature creature, Optional<Double> base, Stat stat) {
        throwIfPresent(base);

        double baseValue = calcWeaponPlusBaseValue(creature, stat);

        // [Square(DEX)] * 5 + lvl + weapon hitbonus;
        final int level = creature.getLevel();
        baseValue += (Math.sqrt(creature.getDEX()) * 5) + level;
        if (level > 69) {
            baseValue += level - 69;
        }
        if (level > 77) {
            baseValue += 1;
        }
        if (level > 80) {
            baseValue += 2;
        }
        if (level > 87) {
            baseValue += 2;
        }
        if (level > 92) {
            baseValue += 1;
        }
        if (level > 97) {
            baseValue += 1;
        }

        if (isPlayer(creature)) {
            // Enchanted gloves bonus
            baseValue += calcEnchantBodyPart(creature, BodyPart.GLOVES);
        }

        // Shadow sense
        if (WorldTimeController.getInstance().isNight()) {
            baseValue += creature.getStats().getAdd(Stat.HIT_AT_NIGHT, 0);
        }

        return Stat.defaultValue(creature, stat, baseValue);
    }

    @Override
    public double calcEnchantBodyPartBonus(int enchantLevel, boolean isBlessed) {
        if (isBlessed) {
            return (0.3 * Math.max(enchantLevel - 3, 0)) + (0.3 * Math.max(enchantLevel - 6, 0));
        }

        return (0.2 * Math.max(enchantLevel - 3, 0)) + (0.2 * Math.max(enchantLevel - 6, 0));
    }
}
