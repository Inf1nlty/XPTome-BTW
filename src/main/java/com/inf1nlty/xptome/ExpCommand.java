package com.inf1nlty.xptome;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.I18n;

public class ExpCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "exp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/exp";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        EntityPlayer player = (EntityPlayer) sender;
        int xp = player.experienceTotal;
        String localized = I18n.getString("commands.exp.message");
        String message = String.format(localized, xp);
        ChatMessageComponent component = ChatMessageComponent.createFromText(message)
                .setColor(EnumChatFormatting.GOLD);
        player.sendChatToPlayer(component);
    }
}