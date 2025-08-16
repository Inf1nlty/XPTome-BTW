package com.inf1nlty.xptome.mixin;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin {
    @Shadow
    public World worldObj;
    @Shadow
    public int xCoord;
    @Shadow
    public int yCoord;
    @Shadow
    public int zCoord;
}