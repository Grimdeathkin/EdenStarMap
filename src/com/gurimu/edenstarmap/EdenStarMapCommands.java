package com.gurimu.edenstarmap;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * Created by Harrison on 02/07/14.
 */
public class EdenStarMapCommands implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        if(cmd.getName().equalsIgnoreCase("AddPlanet")){
            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            if(args.length > 5 || args.length < 1){
                player.sendMessage(ChatColor.RED + "Incorrect usage /addplanet <name> [Price] [item] [inventory location] [Buyable=true/false]");
                return false;
            }
            player.sendMessage(ChatColor.GREEN + "Planet created");
            String name = args[0];
            Location loc = player.getLocation();
            config.set(name + ".world", loc.getWorld().getName());
            config.set(name + ".x", loc.getX());
            config.set(name + ".y", loc.getY());
            config.set(name + ".z", loc.getZ());
            EdenStarMap.plugin.saveConfig();
            if(args.length == 1) return true;
            config.set(name + ".price", Double.parseDouble(args[1]));
            EdenStarMap.plugin.saveConfig();
            if(args.length == 2) return true;
            config.set(name + ".item", args[2]);
            EdenStarMap.plugin.saveConfig();
            if(args.length == 3) return true;
            config.set(name + ".invlocation", Integer.parseInt(args[3]));
            EdenStarMap.plugin.saveConfig();
            if(args.length == 4) return true;
            config.set(name + ".buyable", args[4]);
            List<String> planets = config.getStringList("planets");
            if(!(planets.contains(name))){
                planets.add(name);
            }
            config.set("planets", planets);
            EdenStarMap.plugin.saveConfig();
        }else if(cmd.getName().equalsIgnoreCase("edenreload")){
            EdenStarMap.plugin.reloadConfig();
            sender.sendMessage(ChatColor.RED + "Config reloaded");
        }
        return false;
    }
}
