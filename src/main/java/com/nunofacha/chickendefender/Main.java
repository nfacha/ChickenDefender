package com.nunofacha.chickendefender;

import com.nunofacha.chickendefender.arenas.Arena;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;
    public static ArrayList<Arena> arenas = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = getLogger();
        logger.info("Plugin is starting");
        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        logger.info("Config Version: " + getConfig().getInt("config-version"));
        //Initialize Arenas
        int totalArenas = getArenaCount();
        logger.info("Arena count: " + totalArenas);
        for (int i = 0; i < totalArenas; i++) {
            arenas.add(new Arena(i));
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private int getArenaCount() {
        return this.getConfig().getConfigurationSection("arenas").getKeys(false).size();
    }
}
