package com.inf1nlty.xptome;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class XPTomeItems {
    public static Item xpTome;

    static {
        xpTome = new XPTomeItem(2333)
                .setCreativeTab(CreativeTabs.tabMisc);
    }
}