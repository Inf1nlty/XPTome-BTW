package com.inf1nlty.xptome.mixin.client;

import btw.block.blocks.ArcaneVesselBlock;
import btw.block.tileentity.ArcaneVesselTileEntity;
import com.inf1nlty.xptome.util.ICapacity;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void injectArcaneVesselHUD(float partialTicks, boolean b, int mx, int my, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.objectMouseOver == null) return;

        if (mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
            int x = mc.objectMouseOver.blockX;
            int y = mc.objectMouseOver.blockY;
            int z = mc.objectMouseOver.blockZ;
            World world = mc.theWorld;
            int blockId = world.getBlockId(x, y, z);

            Block block = Block.blocksList[blockId];
            if (block instanceof ArcaneVesselBlock) {
                TileEntity te = world.getBlockTileEntity(x, y, z);

                int currentXP = 0;
                int maxXP = 1000;
                int unit = 20;

                if (te instanceof ICapacity) {
                    currentXP = ((ArcaneVesselTileEntity)te).getContainedTotalExperience();
                    maxXP = ((ICapacity)te).xPTome$getXpCapacity();
                    unit = maxXP / 50;
                } else if (te instanceof ArcaneVesselTileEntity) {
                    currentXP = ((ArcaneVesselTileEntity)te).getContainedTotalExperience();
                }

                float percent = (float) currentXP / (float) maxXP;
                String color;

                if (percent >= 0.75f) {
                    color = "§a";

                } else if (percent >= 0.5f) {
                    color = "§e";

                } else if (percent >= 0.25f) {
                    color = "§6";

                } else {
                    color = "§c";
                }

                String xpStr = color + currentXP + "§f";
                String maxStr = "§7" + maxXP + "§f";
                String text = StatCollector.translateToLocalFormatted("xptome.dragonvessel.exp", xpStr, maxStr);

                String unitColor = (maxXP > 1000) ? "§a" : "§7";
                String unitStr = unitColor + StatCollector.translateToLocalFormatted("xptome.dragonvessel.unit", unit) + "§f";

                FontRenderer fr = mc.fontRenderer;
                ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

                int screenWidth = sr.getScaledWidth();
                int screenHeight = sr.getScaledHeight();
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;
                int textX = centerX - (fr.getStringWidth(text) / 2);
                int textY = centerY - 30;
                int unitX = centerX - (fr.getStringWidth(unitStr) / 2);
                int unitY = textY + 10;

                GL11.glPushMatrix();
                fr.drawStringWithShadow(text, textX, textY, 0x66FFFF);
                fr.drawStringWithShadow(unitStr, unitX, unitY, 0x66FFFF);
                GL11.glPopMatrix();
            }
        }
    }
}