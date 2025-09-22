package com.inf1nlty.xptome.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "addInformation", at = @At("TAIL"))
    private void injectXpCapacityInfo(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced, CallbackInfo ci) {

        if (stack == null) return;

        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return;

        Block block = Block.blocksList[itemBlock.getBlockID()];

        if (block == null || !block.getClass().getName().equals("btw.block.blocks.ArcaneVesselBlock")) return;

        int defaultCapacity = 1000;
        int cap = defaultCapacity;

        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("xpCapacity")) {
            cap = stack.getTagCompound().getInteger("xpCapacity");
        }

        int unit = Math.max(1, cap / 50);

        String localized = StatCollector.translateToLocalFormatted("xptome.dragonvessel.xp_capacity", cap);
        String unitLocalized = StatCollector.translateToLocalFormatted("xptome.dragonvessel.unit", unit);

        if (cap > defaultCapacity) {
            tooltip.add("§a" + localized);
            tooltip.add("§a" + unitLocalized);
        } else {
            tooltip.add("§7" + localized);
            tooltip.add("§7" + unitLocalized);
        }
    }
}