package com.inf1nlty.xptome.mixin;

import btw.block.tileentity.ArcaneVesselTileEntity;
import com.inf1nlty.xptome.util.IXpCapacityUpgradeable;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArcaneVesselTileEntity.class)
public abstract class ArcaneVesselTileEntityMixin implements IXpCapacityUpgradeable {
    @Unique
    private int xpCapacity = 1000;

    @Override
    public int getXpCapacity() { return xpCapacity; }
    @Override
    public void setXpCapacity(int cap) { this.xpCapacity = cap; }
    @Override
    public void increaseXpCapacity(int amount) { this.xpCapacity += amount; }

    @ModifyConstant(
            method = "attemptToSwallowXPOrb",
            constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 1000)
    )
    private int swallowXpCapacity(int original) {
        return this.xpCapacity;
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"))
    private void writeXpCapacityToNBT(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("xpCapacity", this.xpCapacity);
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void readXpCapacityFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey("xpCapacity")) {
            this.xpCapacity = nbt.getInteger("xpCapacity");
        }
    }
}