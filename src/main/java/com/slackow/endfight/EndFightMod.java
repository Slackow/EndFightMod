package com.slackow.endfight;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.StringJoiner;

@Mod(modid = EndFightMod.MODID, version = EndFightMod.VERSION)
public class EndFightMod {
    public static final String MODID = "endfightmod";
    public static final String VERSION = "1.0";

    public static long time;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

    }

    private FMLServerStartingEvent event;

    public static final StringJoiner commands = new StringJoiner("\n");

    private void registerCommand(ICommand command) {
        event.registerServerCommand(command);
        commands.add(command.getCommandUsage(null));
    }


    @EventHandler
    public void init(FMLServerStartingEvent event) throws IOException {
        this.event = event;
        registerCommand(new ResetCommand());
        registerCommand(new GetInvCommand());
        registerCommand(new SetInvCommand());
        registerCommand(new KillCrystalsCommand());
        registerCommand(new HealCommand());
        registerCommand(new DragonHealthCommand());
        registerCommand(new GoodChargeCommand());
        registerCommand(ChargeCommand.command);
        this.event = null;

        if(Files.notExists(GetInvCommand.getInventoryPath())){
            Files.write(GetInvCommand.getInventoryPath(),
                    Collections.singleton("[16777483,16777473,16777571,16777477,1073741842,16777584,16777542,16777491," +
                            "83886504,16777274,16777571,16777571,16777571,16777571,16777571,16777489,402653446," +
                            "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]"));
        }
    }




    @SubscribeEvent
    public void onDragonDamage(LivingHurtEvent e) {
        if (e.entity instanceof EntityDragon) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Dragon damaged by " + e.source.getDamageType() + ": " + e.ammount));
        }
    }

    @SubscribeEvent
    public void onDragonDeath(LivingDeathEvent e) {
        if (e.entity instanceof EntityDragon && EndFightMod.time > 0) {
            long millis = System.currentTimeMillis() - EndFightMod.time;
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
                    new ChatComponentText("Dragon Killed in about " + LocalTime.ofSecondOfDay(millis/1000).format(DateTimeFormatter.ofPattern("mm:ss"))));
            EndFightMod.time = 0;
        }

    }


    public static int itemToInt(ItemStack item) {
        if (item == null) return 0;
        return item.stackSize << 24 | item.getMetadata() << 12 | Item.getIdFromItem(item.getItem());
    }

    public static ItemStack intToItem(int num) {
        if (num == 0) return null;
        return new ItemStack(Item.getItemById(num & 0xFFF), num >>> 24, num >>> 12 & 0xFFF);
    }
}
