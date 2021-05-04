package com.slackow.endfight;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class SetInvCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "setinv";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/setinv";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (iCommandSender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) iCommandSender;
            JsonArray result =
                    Stream.concat(Arrays.stream(player.inventory.mainInventory),
                            Arrays.stream(player.inventory.armorInventory))
                    .mapToInt(EndFightMod::itemToInt)
                    .mapToObj(JsonPrimitive::new)
                    .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
            try {
                Files.write(GetInvCommand.getInventoryPath(), Collections.singleton(new Gson().toJson(result)));
            } catch (IOException e) {
                throw new CommandException("Problem Saving Inventory");
            }
        } else {
            iCommandSender.addChatMessage(new ChatComponentText("You cannot use this command, you must be a player"));
        }
    }
}
