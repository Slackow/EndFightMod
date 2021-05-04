package com.slackow.endfight;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Optional;

import static net.minecraft.util.EnumChatFormatting.RED;

public class ChargeCommand extends CommandBase {

    public static ChargeCommand command = new ChargeCommand();


    @Override
    public String getCommandName() {
        return "charge";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/charge [health]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        if (args.length > 0) {
            DragonHealthCommand.command(sender, args);
        }

        if (sender instanceof EntityPlayer) {
            Optional<EntityDragon> dragon = sender.getEntityWorld().getEntities(EntityDragon.class, d -> !d.isDead)
                    .stream().findFirst();
            if (dragon.isPresent()) {
                EntityDragon entityDragon = dragon.get();
                    try {
                        Field field_70993_bI = FieldUtils.getField(EntityDragon.class, "field_70993_bI", true);
                        FieldUtils.writeField(field_70993_bI == null ? FieldUtils.getField(EntityDragon.class, "target", true) : field_70993_bI, entityDragon, sender, true);
                    } catch (IllegalAccessException e) {
                        throw new CommandException("Failed to force Dragon Charge, might wanna report this");
                    }

                sender.addChatMessage(new ChatComponentText("Forced Dragon Charge"));
            } else if (args.length == 0){
                sender.addChatMessage(new ChatComponentText(RED + "No Dragon Found"));
            }
        }
    }
}
