package com.gurimu.edenstarmap;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harrison on 02/07/14.
 */
public class StarMapCreate implements Listener{

    public static Inventory inventory = Bukkit.createInventory(null, EdenStarMap.plugin.getConfig().getInt("mapsize"), "Starmap");

    public StarMapCreate(Player player){
        createMap(player);
        showMap(player);
    }

    public void createMap(Player player){
        populateMenu(player);
    }

    public void populateMenu(Player player){
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        List<String> planets = config.getStringList("planets");
        for(String name : planets){
            createDisplay(player, "EdenStarMap.access." + name, Material.valueOf(config.getString(name + ".item")), inventory, config.getInt(name + ".invlocation"), name, "");
            ItemStack nextPage = new ItemStack(Material.WRITTEN_BOOK, 1);
            ItemMeta im = nextPage.getItemMeta();
            im.setDisplayName(ChatColor.YELLOW + "Shop");
            nextPage.setItemMeta(im);
            inventory.setItem(EdenStarMap.plugin.getConfig().getInt("mapsize") - 1, nextPage);
        }
    }
    public static void createDisplay(Player player, String permission, Material material, Inventory inv, int Slot, String name, String lore) {
        if(player.hasPermission(permission)){
            name = ChatColor.GREEN + name;
            lore = ChatColor.GREEN + "You can go here";
        }else{
            name = ChatColor.RED + name;
            lore = ChatColor.RED + "You can not go here";
        }
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);

        inv.setItem(Slot, item);

    }
    public void showMap(Player player){
        player.openInventory(inventory);
    }


}
