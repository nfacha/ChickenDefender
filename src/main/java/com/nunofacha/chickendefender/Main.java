package com.nunofacha.chickendefender;

import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import com.nunofacha.chickendefender.arenas.game.GameState;
import com.nunofacha.chickendefender.commands.ChickenJoinCommand;
import com.nunofacha.chickendefender.commands.ChickenSetCommand;
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
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    public static Logger logger;
    public static ArenaManager arenaManager;
    public static Scoreboard scoreboard;
    public static Team sbAttackTeam;
    public static Team sbDefendTeam;

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
        scoreboard = this.getServer().getScoreboardManager().getNewScoreboard();
//        scoreboard.registerNewObjective("Test", "Test", "Test");
        sbAttackTeam = scoreboard.registerNewTeam(ChatColor.RED+"cdAttack");
        sbAttackTeam.setColor(ChatColor.RED);
        sbAttackTeam.setPrefix(ChatColor.RED.toString());
        sbAttackTeam.setSuffix(ChatColor.RED.toString());
        sbAttackTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        sbDefendTeam = scoreboard.registerNewTeam(ChatColor.GREEN+"cdDefend");
        sbDefendTeam.setColor(ChatColor.GREEN);
        sbDefendTeam.setPrefix(ChatColor.GREEN.toString());
        sbDefendTeam.setSuffix(ChatColor.GREEN.toString());
        sbDefendTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getCommand("chickenjoin").setExecutor(new ChickenJoinCommand());
        getCommand("chickenset").setExecutor(new ChickenSetCommand());
        Metrics metrics = new Metrics(this, 10121);
        if(metrics.isEnabled()){
            Main.logger.info("Statistics loaded!");
        }
        try {
            if(getConfig().getInt("config-version") == 1){
                getConfig().set("config-version", 2);
                getConfig().set("update-channel", "master");
                getConfig().save(Main.plugin.getDataFolder()+"/config.yml");
                Main.logger.info("Config version updated to 2");
            }
            if(getConfig().getBoolean("auto-update", true)){
                Main.logger.info("Using the "+getConfig().getString("update-channel")+" update channel!");
                Updater updater = new Updater("https://raw.githubusercontent.com/nfacha/ChickenDefender/"+getConfig().getString("update-channel")+"/meta.json");
            }else{
                Main.logger.info("Auto updating is disabled!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
