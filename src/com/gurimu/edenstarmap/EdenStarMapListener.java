package com.gurimu.edenstarmap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harrison on 02/07/14.
 */
public class EdenStarMapListener implements Listener{

    public HashMap<String, String> planetMaterial = new HashMap<String, String>();


    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Action action = event.getAction();
        if(!(action == Action.RIGHT_CLICK_BLOCK)) return;
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        Material block = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        if(block == null) return;
        Material mat = Material.valueOf(config.getString("mapblock"));
        if(block == mat){
            event.setCancelled(true);
            new StarMapCreate(player);
            new EdenShop(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        List<String> planets = config.getStringList("planets");
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inv = event.getInventory();
        for(String name : planets){
            planetMaterial.put(config.getString(name + ".item"), name);
        }
        if(inv.getName().equals(StarMapCreate.inventory.getName())){
            event.setCancelled(true);
            String currentItem = event.getCurrentItem().getType().toString();
            String planet = planetMaterial.get(currentItem);
            if(planetMaterial.containsKey(currentItem)){
                event.setCancelled(true);
                player.closeInventory();
                if(player.hasPermission("edenstarmap.access." + planet)){
                    teleportPlayer(planet, player);
                }else{
                    player.sendMessage(ChatColor.RED + "You can not go there");
                }
            }else if(event.getCurrentItem().getType() == Material.WRITTEN_BOOK){
                event.setCancelled(true);
                player.closeInventory();
                player.openInventory(EdenShop.shop);
            }

        }else if(inv.getName().equals(EdenShop.shop.getName())){
            String currentItem = event.getCurrentItem().getType().toString();
            String planet = planetMaterial.get(currentItem);
            if(planetMaterial.containsKey(currentItem)){
                event.setCancelled(true);
                player.closeInventory();
                if(!(player.hasPermission("edenstarmap.access." + planet))){
                    EconomyResponse r = EdenStarMap.econ.withdrawPlayer(player, config.getDouble(planet + ".price"));
                    if(r.transactionSuccess()){
                        EdenStarMap.perms.playerAdd(null, player, "edenstarmap.access." + planet);
                        player.sendMessage(ChatColor.GREEN + "You can now access " + planet);
                    }else{
                        player.sendMessage(ChatColor.RED + "You can not afford access to " + planet);
                    }
                }
            }else if(event.getCurrentItem().getType() == Material.WRITTEN_BOOK){
                event.setCancelled(true);
                player.closeInventory();
                new StarMapCreate(player);
            }
        }
    }

    public void teleportPlayer(String planet, Player player){
        if(player.hasPermission("EdenStarMap.access." + planet)){
            Location loc = getLocation(planet);
            player.teleport(loc);
        }else{
            player.sendMessage("You can not visit this planet");
        }
    }

    public Location getLocation(String planet){
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        World world = Bukkit.getServer().getWorld(config.getString(planet + ".world"));
        double x = config.getDouble(planet + ".x");
        double y = config.getDouble(planet + ".y");
        double z = config.getDouble(planet + ".z");
        return new Location(world, x, y ,z);
    }
}
