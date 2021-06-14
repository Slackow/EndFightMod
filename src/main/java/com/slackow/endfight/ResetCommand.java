package com.slackow.endfight;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResetCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "reset";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/reset";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos p_addTabCompletionOptions_3_) {
        if (args.length == 1 && "options".startsWith(args[0])) {
            return Collections.singletonList("options");
        }
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) throws CommandException {

        if (args.length > 0 && "options".equals(args[0])) {
            iCommandSender.addChatMessage(new ChatComponentText(EndFightMod.commands.toString()));
            return;
        }

        if (iCommandSender instanceof EntityPlayerMP) {


            EntityPlayerMP player = (EntityPlayerMP) iCommandSender;

            WorldServer overworld = DimensionManager.getWorld(0);
            MinecraftServer mcserver = overworld.getMinecraftServer();
            List<EntityPlayerMP> players = mcserver.getConfigurationManager().playerEntityList;

            for (EntityPlayerMP p : new ArrayList<>(players)) {
                if (p.dimension == 1) {
                    p.inventory.mainInventory = new ItemStack[36];
                    p.inventory.armorInventory = new ItemStack[4];
                    p.inventory.inventoryChanged = true;
                    p.setGameType(WorldSettings.GameType.CREATIVE);
                    p.travelToDimension(0);
                }
            }

            if (new File(DimensionManager.getCurrentSaveRootDirectory(), "DIM1").exists()) {


                WorldServer end = DimensionManager.getWorld(1);
                if (end != null) {
                    MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(end));
                    end.flush();
                    DimensionManager.setWorld(1, null);
                    end.flush();
                }
                try {
                    FileUtils.deleteDirectory(new File(DimensionManager.getCurrentSaveRootDirectory(), "DIM1"));
                } catch (IOException e) {
                    throw new CommandException("delete.directory.failed");
                } finally {
                    player.addChatMessage(new ChatComponentText("Completely Removed the End Dimension"));
                }
            } else {
                player.setHealth(20f);
                player.setFire(-1);
                player.addPotionEffect(new PotionEffect(11, 4, 255, true, false));
                player.getFoodStats().setFoodLevel(20);
                player.getFoodStats().setFoodSaturationLevel(4);
                player.setGameType(WorldSettings.GameType.SURVIVAL);
                GetInvCommand.giveInventory(player);
                player.travelToDimension(1);
                player.addChatComponentMessage(new ChatComponentText("Sent to End"));
                EndFightMod.time = System.currentTimeMillis();
            }
        }
    }
}
