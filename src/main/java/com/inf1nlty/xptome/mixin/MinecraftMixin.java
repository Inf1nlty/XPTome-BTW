package com.inf1nlty.xptome.mixin;

import com.inf1nlty.xptome.HandshakeClient;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Polls custom keys each client tick.
 * B -> open system shop
 * G -> open global shop
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "runTick", at = @At("TAIL"))
    private void shop$handleHotkeys(CallbackInfo ci) {
        HandshakeClient.onClientTick();
    }
}