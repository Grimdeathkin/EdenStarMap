package com.gurimu.edenstarmap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Harrison on 02/07/14.
 */
public class EdenStarMap extends JavaPlugin {

    public static EdenStarMap plugin;
    public FileConfiguration config = this.getConfig();
    public static Economy econ = null;
    public static Permission perms = null;

    @Override
    public void onEnable(){
        this.plugin = this;
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        this.getServer().getPluginManager().registerEvents(new EdenStarMapListener(), this);
        this.getCommand("addplanet").setExecutor(new EdenStarMapCommands());
        this.getCommand("edenreload").setExecutor(new EdenStarMapCommands());
    }

    @Override
    public void onDisable(){

    }

    public boolean setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
