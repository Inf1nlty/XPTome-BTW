package com.inf1nlty.xptome;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;

import java.util.List;
import java.util.Random;

public class XPTomeItem extends Item {
    public static final int MAX_XP = 1395;
    private final Random random = new Random();

    public XPTomeItem(int id) {
        super(id);
        this.maxStackSize = 1;
        this.setUnlocalizedName("xp_tome");
        this.setTextureName("xp_tome");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        int storedXP = getStoredXP(stack);

        if (player.isSneaking()) {
            int playerXP = EnchantmentUtils.getPlayerXP(player);
            int leftCapacity = MAX_XP - storedXP;
            int toStore = Math.min(playerXP, leftCapacity);

            if (toStore > 0) {
                setStoredXP(stack, storedXP + toStore);
                EnchantmentUtils.addPlayerXP(player, -toStore);
                if (world.isRemote) {
                    float pitch = (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F;
                    player.playSound("random.classic_hurt", 1.0F, pitch);
                }
            }
        } else {
            if (storedXP > 0) {
                int oldLevel = player.experienceLevel;

                EnchantmentUtils.addPlayerXP(player, storedXP);
                setStoredXP(stack, 0);

                int newLevel = player.experienceLevel;

                if (oldLevel < 30 && newLevel >= 30) {

                    float pitchMultiplier = newLevel > 30 ? 1.0F : (newLevel / 30.0F);
                    player.playSound("random.levelup", pitchMultiplier * 0.75F, 1.0F);
                } else {
                    float pitch = (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F;
                    player.playSound("random.orb", 0.1F, pitch);
                }
            }
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced) {
        int storedXP = getStoredXP(stack);
        float percent = (float) storedXP / (float) MAX_XP;
        String color;
        if(percent >= 0.75f) {
            color = "§a";
        } else if(percent >= 0.5f) {
            color = "§e";
        } else if(percent >= 0.25f) {
            color = "§6";
        } else {
            color = "§c";
        }
        String colorMax = "§7" + MAX_XP + "§f";
        info.add(StatCollector.translateToLocal("xpbook.tooltip.1"));
        info.add(StatCollector.translateToLocal("xpbook.tooltip.2"));
        info.add(String.format(StatCollector.translateToLocal("xpbook.tooltip.3"), color + storedXP + "§f", colorMax));
    }

    private static int getStoredXP(ItemStack stack) {
        NBTTagCompound tag = stack.stackTagCompound;
        return tag != null ? tag.getInteger("StoredXP") : 0;
    }
    private static void setStoredXP(ItemStack stack, int xp) {
        NBTTagCompound tag = stack.stackTagCompound;
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger("StoredXP", xp);
    }
}