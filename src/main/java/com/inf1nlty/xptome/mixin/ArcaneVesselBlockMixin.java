package com.inf1nlty.xptome.mixin;

import btw.block.blocks.ArcaneVesselBlock;
import com.inf1nlty.xptome.XPTomeItems;
import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import java.util.List;

@Mixin(ArcaneVesselBlock.class)
public abstract class ArcaneVesselBlockMixin {

    @SuppressWarnings("unchecked")
    @Unique
    public boolean attemptToAffectBlockWithSoul(World world, int x, int y, int z) {
        if (world.isRemote) return false;

        double searchRadius = 0.5D;
        List<EntityItem> items = world.getEntitiesWithinAABB(
                EntityItem.class,
                AxisAlignedBB.getAABBPool().getAABB(
                        x + 0.5 - searchRadius, y + 1.0, z + 0.5 - searchRadius,
                        x + 0.5 + searchRadius, y + 1.5, z + 0.5 + searchRadius
                )
        );

        for (EntityItem itemEntity : items) {
            ItemStack stack = itemEntity.getEntityItem();
            if (stack != null && stack.getItem() == Item.enchantedBook) {
                itemEntity.setDead();
                ItemStack xpTome = new ItemStack(XPTomeItems.xpTome, 1);
                EntityItem result = new EntityItem(
                        world,
                        x + 0.5D, y + 1.1D, z + 0.5D,
                        xpTome
                );
                world.spawnEntityInWorld(result);
                return true;
            }
        }
        return false;
    }

    @ModifyConstant(
            method = "getComparatorInputOverride",
            constant = @org.spongepowered.asm.mixin.injection.Constant(floatValue = 1000.0F)
    )
    private float dynamicXpCapacityForComparator(float original, World world, int x, int y, int z, int side) {
        try {
            Object tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof ICapacity) {
                return ((ICapacity) tile).xPTome$getXpCapacity();
            }
        } catch (Throwable ignore) {}
        return 1000.0F;
    }
}