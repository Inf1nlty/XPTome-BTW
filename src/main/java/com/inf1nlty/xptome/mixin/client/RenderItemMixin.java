package com.inf1nlty.xptome.mixin.client;

import com.inf1nlty.xptome.XPTomeItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderItem;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.TextureManager;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Inject(method = "renderItemOverlayIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/TextureManager;Lnet/minecraft/src/ItemStack;II)V", at = @At("TAIL"))
    private void xptome$renderXPBar(FontRenderer fontRenderer, TextureManager textureManager, ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack == null || !(stack.getItem() instanceof XPTomeItem)) return;

        int storedXP = XPTomeItem.getStoredXP(stack);
        int maxXP = XPTomeItem.MAX_XP;

        if (storedXP < maxXP) {
            float percent = (float) storedXP / (float) maxXP;
            int barLength = (int)Math.floor(13.0D * percent);
            int barColor = getXPBarColor(percent);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator t = Tessellator.instance;
            renderQuad(t, x + 2, y + 13, 13, 2, 0x000000);
            renderQuad(t, x + 2, y + 13, 12, 1, 0x404040);

            if (barLength > 0) {
                renderQuad(t, x + 2, y + 13, barLength, 1, barColor);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1,1,1,1);
        }
    }

    @Unique
    private int getXPBarColor(float percent) {
        int red = (int)(255.0F * (1.0F - percent));
        int green = 255 - red;
        return (red << 16) | (green << 8);
    }

    @Unique
    private void renderQuad(Tessellator tess, int x, int y, int width, int height, int color) {
        tess.startDrawingQuads();
        tess.setColorOpaque_I(color);
        tess.addVertex(x,         y,          0.0D);
        tess.addVertex(x,         y + height, 0.0D);
        tess.addVertex(x + width, y + height, 0.0D);
        tess.addVertex(x + width, y,          0.0D);
        tess.draw();
    }
}