package com.slackow.endfight;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ChatComponentText;

import java.util.List;

import static net.minecraft.util.EnumChatFormatting.RED;

public class KillCrystalsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "killcrystals";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/killcrystals";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) throws CommandException {
        List<EntityEnderCrystal> entities = sender.getEntityWorld()
                .getEntities(EntityEnderCrystal.class, crystal -> !crystal.isDead);
        if (entities.isEmpty()) {
            sender.addChatMessage(new ChatComponentText(RED + "No End Crystals Found"));
        } else {
            sender.addChatMessage(new ChatComponentText("Killed " + entities.size() + " End Crystals"));
        }
        entities.forEach(Entity::setDead);
    }
}
