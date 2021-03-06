package com.nunofacha.chickendefender;

import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import com.nunofacha.chickendefender.arenas.game.GameState;
import com.nunofacha.chickendefender.commands.ChickenJoinCommand;
import com.nunofacha.chickendefender.commands.ChickenKitCommand;
import com.nunofacha.chickendefender.commands.ChickenLeaveCommand;
import com.nunofacha.chickendefender.commands.ChickenSetCommand;
import com.nunofacha.chickendefender.kits.Kit;
import com.nunofacha.chickendefender.listeners.GlobalListener;
import com.nunofacha.chickendefender.listeners.SignListener;
import com.nunofacha.chickendefender.updater.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;
    public static ArenaManager arenaManager;
    public static Scoreboard scoreboard;
    public static Team sbAttackTeam;
    public static Team sbDefendTeam;
    public static HashMap<String, Kit> kits = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = getLogger();
        logger.info("Plugin is starting");
        saveDefaultConfig();
        logger.info("Config Version: " + getConfig().getInt("config-version"));
        arenaManager = new ArenaManager();
        initScoreboard();
        registerEvents();
        registerCommands();
        updateCheck();
        initKits();
        Metrics metrics = new Metrics(this, 10121);
        if (metrics.isEnabled()) {
            Main.logger.info("Statistics loaded!");
        }
        updateConfig();

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

    private void initScoreboard() {
        scoreboard = this.getServer().getScoreboardManager().getNewScoreboard();
        sbAttackTeam = scoreboard.registerNewTeam(ChatColor.RED + "cdAttack");
        sbAttackTeam.setColor(ChatColor.RED);
        sbAttackTeam.setPrefix(ChatColor.RED.toString());
        sbAttackTeam.setSuffix(ChatColor.RED.toString());
        sbAttackTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        sbDefendTeam = scoreboard.registerNewTeam(ChatColor.GREEN + "cdDefend");
        sbDefendTeam.setColor(ChatColor.GREEN);
        sbDefendTeam.setPrefix(ChatColor.GREEN.toString());
        sbDefendTeam.setSuffix(ChatColor.GREEN.toString());
        sbDefendTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }

    private void registerCommands() {
        getCommand("chickenjoin").setExecutor(new ChickenJoinCommand());
        getCommand("chickenset").setExecutor(new ChickenSetCommand());
        getCommand("chickenleave").setExecutor(new ChickenLeaveCommand());
        getCommand("chickenkit").setExecutor(new ChickenKitCommand());
    }

    private void updateConfig() {
        try {
            if (getConfig().getInt("config-version") == 1) {
                getConfig().set("config-version", 2);
                getConfig().set("update-channel", "master");
                getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.logger.info("Config version updated to 2");
            }
            if (getConfig().getInt("config-version") == 2) {
                for (Arena arena : ArenaManager.getArenas()) {
                    getConfig().set(arena.getConfigPath() + ".clear-inventory", true);
                }
                getConfig().set("config-version", 3);
                getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.logger.info("Config version updated to 3");
            }
            if (getConfig().getInt("config-version") == 3) {
                for (Arena arena : ArenaManager.getArenas()) {
                    getConfig().set(arena.getConfigPath() + ".team-helmet", false);
                    getConfig().set(arena.getConfigPath() + ".player-glow", true);
                }
                getConfig().set("config-version", 4);
                getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.logger.info("Config version updated to 4");
            }
            if (getConfig().getInt("config-version") == 4) {
                for (Arena arena : ArenaManager.getArenas()) {
                    getConfig().set(arena.getConfigPath() + ".enabled", true);
                }
                getConfig().set("config-version", 5);
                getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.logger.info("Config version updated to 5");
            }
            if (getConfig().getInt("config-version") == 5) {
                for (Arena arena : ArenaManager.getArenas()) {
                    getConfig().set(arena.getConfigPath() + ".friendly-fire", false);
                }
                getConfig().set("config-version", 6);
                getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.logger.info("Config version updated to 6");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCheck() {
        try {

            if (getConfig().getBoolean("auto-update", true)) {
                Main.logger.info("Using the " + getConfig().getString("update-channel") + " update channel!");
                Updater updater = new Updater("https://raw.githubusercontent.com/nfacha/ChickenDefender/" + getConfig().getString("update-channel") + "/meta.json");
            } else {
                Main.logger.info("Auto updating is disabled!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initKits() {
        kits.clear();
        Set<String> kitNames = getConfig().getConfigurationSection("kits").getKeys(false);
        for (String kitName : kitNames) {
            kits.put(kitName, new Kit(kitName));
        }
    }

}
