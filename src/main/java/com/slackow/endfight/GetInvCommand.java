package com.slackow.endfight;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GetInvCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "getinv";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/getinv";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            giveInventory(player);
        }
    }

    public static void giveInventory(EntityPlayer player) throws CommandException {
        Path path = getInventoryPath();
        try {
            String content = Files.lines(path).collect(Collectors.joining());
            ItemStack[] inv = StreamSupport.stream(new JsonParser().parse(content).getAsJsonArray().spliterator(), false)
                    .mapToInt(JsonElement::getAsInt)
                    .mapToObj(EndFightMod::intToItem)
                    .toArray(ItemStack[]::new);
            for (int i = 0; i < inv.length; i++) {
                player.inventory.setInventorySlotContents(i, inv[i]);
            }
            player.inventory.inventoryChanged = true;
        } catch (IOException | JsonSyntaxException e) {
            throw new CommandException("Unable To Read inv.txt");
        }
    }

    public static Path getInventoryPath() {
        return Minecraft.getMinecraft().mcDataDir.toPath().resolve("inv.txt");
    }
}
