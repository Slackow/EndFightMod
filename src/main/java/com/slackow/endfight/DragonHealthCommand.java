package com.slackow.endfight;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ChatComponentText;

import java.util.Optional;

import static net.minecraft.util.EnumChatFormatting.RED;

public class DragonHealthCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "dragonhealth";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/dragonhealth [new Health]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        command(sender, args);
    }

    public static void command(ICommandSender sender, String[] args) {
        Optional<EntityDragon> dragon = sender.getEntityWorld().getEntities(EntityDragon.class, d -> !d.isDead).stream().findFirst();
        if (dragon.isPresent() && args.length > 0) {
            try {
                dragon.get().setHealth(Math.min(200, Math.max(Float.parseFloat(args[0]), 0)));
            } catch (NumberFormatException e) {
                sender.addChatMessage(new ChatComponentText(RED + "Not a valid health"));
            }
        }
        sender.addChatMessage(new ChatComponentText(dragon.map(entityDragon ->"Dragon Health is: " + entityDragon.getHealth()).orElseGet(() -> RED + "No Dragon Found")));
    }
}
