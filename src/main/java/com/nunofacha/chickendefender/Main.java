package com.nunofacha.chickendefender;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = getLogger();
        logger.info("Plugin is stating");
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
