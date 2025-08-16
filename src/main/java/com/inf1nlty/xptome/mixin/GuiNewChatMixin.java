package com.inf1nlty.xptome.mixin;

import net.minecraft.src.GuiNewChat;
import net.minecraft.src.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = GuiNewChat.class)
public class GuiNewChatMixin {

    /**
     * Only intercepts known keys to avoid interfering with other mods.
     */
    @ModifyVariable(method = "drawChat", at = @At(value = "STORE", ordinal = 0), name = "var17")
    private String localizeExpMessage(String var17) {
        if (var17 == null) return null;

        String[] myKeys = {
                "commands.exp.message",
                "xpbook.upgrade.success"
        };

        for (String keyPrefix : myKeys) {
            if (var17.contains(keyPrefix)) {
                int idx = var17.indexOf(keyPrefix);
                int pipeIdx = var17.indexOf("|", idx);

                String key = pipeIdx > -1 ? var17.substring(idx, pipeIdx) : var17.substring(idx);
                String localized = I18n.getString(key);

                if (pipeIdx > -1) {
                    String params = var17.substring(pipeIdx + 1);
                    String[] paramPairs = params.split("\\|");
                    for (String pair : paramPairs) {
                        String[] kv = pair.split("=", 2);
                        if (kv.length == 2) {
                            localized = localized.replace("{" + kv[0] + "}", kv[1]);
                        }
                    }
                }
                return var17.replace(var17.substring(idx), localized);
            }
        }
        return var17;
    }
}