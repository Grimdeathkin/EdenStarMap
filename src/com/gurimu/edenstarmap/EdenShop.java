package com.gurimu.edenstarmap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harrison on 11/07/14.
 */
public class EdenShop {
    public static Inventory shop = Bukkit.createInventory(null, EdenStarMap.plugin.getConfig().getInt("mapsize"), "Shop");

    public EdenShop(Player player){
        populateMenu(player);
    }

    public void populateMenu(Player player){
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        List<String> planets = config.getStringList("planets");
        for(String name : planets){
            if(config.getString(name + ".buyable").equals("true"))
            createDisplay(player, "EdenStarMap.access." + name, Material.valueOf(config.getString(name + ".item")), shop, config.getInt(name + ".invlocation"), name, "");
            ItemStack nextPage = new ItemStack(Material.WRITTEN_BOOK, 1);
            ItemMeta im = nextPage.getItemMeta();
            im.setDisplayName(ChatColor.YELLOW + "Star Map");
            nextPage.setItemMeta(im);
            shop.setItem(EdenStarMap.plugin.getConfig().getInt("mapsize") - 1, nextPage);
        }
    }
    public static void createDisplay(Player player, String permission, Material material, Inventory inv, int Slot, String name, String lore) {
        FileConfiguration config = EdenStarMap.plugin.getConfig();
        if(player.hasPermission(permission)){
            name = ChatColor.GREEN + name;
            lore = ChatColor.GREEN + "You can go here already";
        }else{
            lore = ChatColor.RED + "You can pay $" + config.getDouble(name + ".price") + " to go here";
            name = ChatColor.RED + name;
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
        player.openInventory(shop);
    }
}
