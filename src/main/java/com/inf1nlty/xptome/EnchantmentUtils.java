package com.inf1nlty.xptome;

import net.minecraft.src.EntityPlayer;

public class EnchantmentUtils {

    public static int getPlayerXP(EntityPlayer player) {
        return getExperienceForLevel(player.experienceLevel) +
                Math.round(player.experience * xpBarCap(player.experienceLevel));
    }

    public static void addPlayerXP(EntityPlayer player, int amount) {
        int experience = getPlayerXP(player) + amount;
        experience = Math.max(0, experience);

        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        int xpCap = xpBarCap(player.experienceLevel);
        player.experience = xpCap == 0 ? 0F : (float)(experience - expForLevel) / (float)xpCap;
        player.experienceTotal = experience;
    }

    public static int getExperienceForLevel(int level) {
        if (level < 15) {
            return 17 * level;
        } else if (level < 30) {
            return (int) (1.5 * level * level - 29.5 * level + 360);
        } else {
            return (int) (3.5 * level * level - 151.5 * level + 2220);
        }
    }

    public static int getLevelForExperience(int xp) {
        if (xp < getExperienceForLevel(15)) {
            return xp / 17;
        } else if (xp < getExperienceForLevel(30)) {
            // 1.5x^2 - 29.5x + 360 = xp
            // x = [29.5 + sqrt(29.5^2 - 6*(360-xp))] / 3
            return (int)((29.5 + Math.sqrt(29.5 * 29.5 - 6 * (360 - xp))) / 3);
        } else {
            // 3.5x^2 - 151.5x + 2220 = xp
            // x = [151.5 + sqrt(151.5^2 - 14*(2220-xp))] / 7
            return (int)((151.5 + Math.sqrt(151.5 * 151.5 - 14 * (2220 - xp))) / 7);
        }
    }

    public static int xpBarCap(int level) {
        if (level >= 30) {
            return 62 + (level - 30) * 7;
        } else if (level >= 15) {
            return 17 + (level - 15) * 3;
        } else {
            return 17;
        }
    }
}