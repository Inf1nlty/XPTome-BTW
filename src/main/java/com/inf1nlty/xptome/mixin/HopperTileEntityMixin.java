package com.inf1nlty.xptome.mixin;

import btw.block.tileentity.HopperTileEntity;
import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HopperTileEntity.class)
public abstract class HopperTileEntityMixin extends TileEntityMixin {
    @ModifyConstant(method = "attemptToEjectXPIntoArcaneVessel", constant = @Constant(intValue = 1000), remap = false
    )
    private int dynamicArcaneVesselCapacity(int original, int iTargetI, int iTargetJ, int iTargetK) {
        TileEntity targetTile = worldObj.getBlockTileEntity(iTargetI, iTargetJ, iTargetK);
        if (targetTile instanceof ICapacity) {
            return ((ICapacity)targetTile).xPTome$getXpCapacity();
        }
        return original;
    }
}