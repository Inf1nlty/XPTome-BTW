package com.inf1nlty.xptome.mixin;

import com.inf1nlty.xptome.HandshakeServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    private void shop$onServerTick(CallbackInfo ci) {
        HandshakeServer.onServerTick();
    }
}