package com.nunofacha.chickendefender;

import com.nunofacha.chickendefender.arenas.ArenaManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;
    public static ArenaManager arenaManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = getLogger();
        logger.info("Plugin is starting");
        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        logger.info("Config Version: " + getConfig().getInt("config-version"));
        arenaManager = new ArenaManager();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
