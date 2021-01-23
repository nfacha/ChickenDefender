package com.nunofacha.chickendefender;

import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import com.nunofacha.chickendefender.arenas.game.GameState;
import com.nunofacha.chickendefender.commands.ChickenJoinCommand;
import com.nunofacha.chickendefender.listeners.GlobalListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;
    public static ArenaManager arenaManager;

    @SuppressWarnings("ConstantConditions")
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
        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getCommand("chickenjoin").setExecutor(new ChickenJoinCommand());
    }

    @Override
    public void onDisable() {
        for (Arena arena :
                ArenaManager.getArenas()) {
            if (arena.getState() == GameState.LIVE) {
                arena.finish();
            }
        }


    }
}
