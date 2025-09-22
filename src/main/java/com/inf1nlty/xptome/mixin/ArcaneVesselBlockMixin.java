package com.inf1nlty.xptome.mixin;

import btw.block.blocks.ArcaneVesselBlock;
import btw.block.tileentity.ArcaneVesselTileEntity;
import com.inf1nlty.xptome.XPTomeItems;
import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
                        x + 0.5 + searchRadius, y + 1.5, z + 0.5 + searchRadius));

        for (EntityItem itemEntity : items) {
            ItemStack stack = itemEntity.getEntityItem();
            if (stack != null && stack.getItem() == Item.enchantedBook) {
                itemEntity.setDead();
                ItemStack xpTome = new ItemStack(XPTomeItems.xpTome, 1);
                EntityItem result = new EntityItem(world, x + 0.5D, y + 1.1D, z + 0.5D, xpTome);
                world.spawnEntityInWorld(result);
                return true;
            }
        }

        return false;
    }

    @ModifyConstant(method = "getComparatorInputOverride", constant = @org.spongepowered.asm.mixin.injection.Constant(floatValue = 1000.0F))
    private float dynamicXpCapacityForComparator(float original, World world, int x, int y, int z, int side) {
        try {
            Object tile = world.getBlockTileEntity(x, y, z);
            if (tile instanceof ICapacity) {
                return ((ICapacity) tile).xPTome$getXpCapacity();
            }
        } catch (Throwable ignore) {}
        return 1000.0F;
    }

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void injectXpCapacityDrop(World world, int x, int y, int z, int blockID, int meta, CallbackInfo ci) {

        if (world.isRemote) return;

        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof ArcaneVesselTileEntity vessel) {
            vessel.ejectContentsOnBlockBreak();
        }

        if (te instanceof ICapacity) {
            int xpCapacity = ((ICapacity) te).xPTome$getXpCapacity();

            ItemStack drop = new ItemStack(blockID, 1, meta);

            if (xpCapacity != 1000) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("xpCapacity", xpCapacity);
                drop.setTagCompound(tag);
            }

            EntityItem entity = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drop);
            world.spawnEntityInWorld(entity);
        }
        ci.cancel();
    }
}