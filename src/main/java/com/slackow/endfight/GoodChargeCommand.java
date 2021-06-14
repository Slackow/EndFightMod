package com.slackow.endfight;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

import java.util.Optional;

import static net.minecraft.util.EnumChatFormatting.RED;

public class GoodChargeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "goodcharge";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/goodcharge [distance, height] [health]";
    }


    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            Optional<EntityDragon> dragon = sender.getEntityWorld().getEntities(EntityDragon.class, d -> !d.isDead)
                    .stream().findFirst();
            if (dragon.isPresent()) {

                int dist = 75;
                int height = 24;
                if (args.length >= 2) {
                    try {
                        dist = Integer.parseUnsignedInt(args[0]);
                        height = Integer.parseUnsignedInt(args[1]);
                    } catch (NumberFormatException e) {
                        throw new CommandException("invalid.input");
                    }
                }

                float yaw = ((EntityPlayer) sender).getRotationYawHead();
                double sin = Math.sin((yaw + 90) * Math.PI / 180f),
                        cos = Math.cos((yaw + 90) * Math.PI / 180f);

                Vec3 playerLocation = sender.getPositionVector();
                EntityDragon entityDragon = dragon.get();
                entityDragon.setPositionAndRotation(playerLocation.xCoord + cos * dist,
                        playerLocation.yCoord + height,
                        playerLocation.zCoord + sin * dist,
                        0,
                        (yaw + 360) % 360 - 180);
                entityDragon.motionX = 0;
                entityDragon.motionY = 0;
                entityDragon.motionZ = 0;
                entityDragon.setMoveForward(0);
                if ((args.length & 1) == 0) {
                    ChargeCommand.command.processCommand(sender, new String[0]);
                } else if (args.length > 0) {
                    ChargeCommand.command.processCommand(sender, new String[]{args[args.length - 1]});
                }
            } else {
                sender.addChatMessage(new ChatComponentText(RED + "No Dragon Found"));
            }
        }
    }
}
