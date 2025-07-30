package com.inf1nlty.xptome;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class XPTomeItems {
    public static Item xpTome;

    public static void registerItems() {

        xpTome = new XPTomeItem(23333)
                .setCreativeTab(CreativeTabs.tabMisc);
        Item.itemsList[xpTome.itemID] = xpTome;
    }
}