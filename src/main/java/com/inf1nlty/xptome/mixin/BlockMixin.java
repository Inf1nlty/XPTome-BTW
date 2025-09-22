package com.inf1nlty.xptome.mixin;

import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "onBlockPlacedBy", at = @At("TAIL"))
    private void injectXpCapacityOnPlace(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack, CallbackInfo ci) {
        if (!this.getClass().getName().equals("btw.block.blocks.ArcaneVesselBlock")) return;

        if (world.isRemote) return;

        if (stack == null || !stack.hasTagCompound()) return;

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey("xpCapacity")) return;

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof ICapacity) {
            ((ICapacity) te).xPTome$setXpCapacity(tag.getInteger("xpCapacity"));
        }
    }

    @Inject(method = "dropBlockAsItem_do", at = @At("HEAD"), cancellable = true)
    private void cancelArcaneVesselDrop(World world, int x, int y, int z, ItemStack stack, CallbackInfo ci) {
        if (this.getClass().getName().equals("btw.block.blocks.ArcaneVesselBlock")) {
            ci.cancel();
        }
    }
}