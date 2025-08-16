package com.inf1nlty.xptome;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.ChatMessageComponent;

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
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        int xp = player.experienceTotal;
        ChatMessageComponent message = ChatMessageComponent.createFromText("commands.exp.message|xp=" + xp);
        message.setColor(EnumChatFormatting.GOLD);
        player.sendChatToPlayer(message);
    }
}