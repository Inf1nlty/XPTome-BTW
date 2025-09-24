package com.inf1nlty.xptome.mixin;

import btw.block.tileentity.ArcaneVesselTileEntity;
import com.inf1nlty.xptome.util.IAbsorbedByDispenser;
import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArcaneVesselTileEntity.class)
public abstract class ArcaneVesselTileEntityMixin extends TileEntityMixin implements ICapacity, IAbsorbedByDispenser {

    @Unique
    private int xpCapacity = 1000;
    @Unique
    private boolean xptome$beingAbsorbedByDispenser = false;

    @Shadow(remap = false) public int containedDragonExperience;
    @Shadow(remap = false) public int containedRegularExperience;
    @Shadow(remap = false) public int visualExperienceLevel;
    @Shadow public abstract void writeToNBT(NBTTagCompound nbt);
    @Shadow public abstract void readFromNBT(NBTTagCompound nbt);

    @Override
    public int xPTome$getXpCapacity() { return xpCapacity; }
    @Override
    public void xPTome$setXpCapacity(int cap) { this.xpCapacity = cap; }
    @Override
    public void xPTome$increaseXpCapacity(int amount) { this.xpCapacity += amount; }

    @Unique
    private int getXpEjectUnitSize() {
        return Math.max(1, this.xpCapacity / 50);
    }

    @ModifyConstant(method = "attemptToSwallowXPOrb", constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 1000))
    private int swallowXpCapacity(int original) {
        return this.xpCapacity;
    }

    @ModifyConstant(method = "attemptToSpillXPFromInv", constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 20), remap = false)
    private int dynamicXpEjectUnitSize1(int original) {
        return getXpEjectUnitSize();
    }

    @ModifyConstant(method = "ejectContentsOnBlockBreak", constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 20), remap = false)
    private int dynamicXpEjectUnitSize2(int original) {
        return getXpEjectUnitSize();
    }

    @Inject(method = "validateVisualExperience", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectVisualExperience(CallbackInfo ci) {
        int iTotalExperience = containedDragonExperience + containedRegularExperience;
        int iNewVisualExperience = (int)(10.0F * ((float)iTotalExperience / (float)this.xPTome$getXpCapacity()));
        if (iTotalExperience > 0 && iNewVisualExperience == 0) {
            iNewVisualExperience = 1;
        }
        if (iNewVisualExperience != visualExperienceLevel) {
            visualExperienceLevel = iNewVisualExperience;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        ci.cancel();
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

    @Inject(method = "setContainedRegularExperience", at = @At("TAIL"), remap = false)
    private void injectSyncRegularXP(int iExperience, CallbackInfo ci) {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Inject(method = "setContainedDragonExperience", at = @At("TAIL"), remap = false)
    private void injectSyncDragonXP(int iExperience, CallbackInfo ci) {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    /**
     * @author Inf1nlty
     * @reason Sync all NBT data including xpCapacity to client.
     */
    @Overwrite
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    /**
     * @author Inf1nlty
     * @reason Read all NBT data from packet for client sync.
     */
    @Overwrite
    public void readNBTFromPacket(NBTTagCompound tag) {
        this.readFromNBT(tag);
        if (this.worldObj != null) {
            this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public boolean xptome$isBeingAbsorbedByDispenser() {
        return xptome$beingAbsorbedByDispenser;
    }
    @Override
    public void xptome$setBeingAbsorbedByDispenser(boolean value) {
        xptome$beingAbsorbedByDispenser = value;
    }

}