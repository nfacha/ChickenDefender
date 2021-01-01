package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import org.bukkit.Location;
import org.bukkit.World;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions"})
public class Arena {
    private final int arenaId;
    private final String configPath;
    private String name;
    private boolean teamSelection;
    private int minPlayers;
    private int maxPlayers;
    private World world;
    private Location attackingSpawn;
    private Location defendingSpawn;
    private Location chickenSpawn;

    public Arena(int arenaId) {
        this.arenaId = arenaId;
        this.configPath = "arenas." + arenaId;
        //Load config
        this.name = Main.plugin.getConfig().getString(this.configPath + ".name");
        this.teamSelection = Main.plugin.getConfig().getBoolean(this.configPath + ".team-selection");
        this.minPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.min");
        this.maxPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.max");
        this.world = Main.plugin.getServer().getWorld(Main.plugin.getConfig().getString(this.configPath + ".locations.world"));
        this.attackingSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.attacking.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.attacking.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.attacking.z"));
        this.defendingSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.defending.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.defending.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.defending.z"));
        this.chickenSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.chicken.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.chicken.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.chicken.z"));

        Main.logger.info("Arena " + this.name + " was loaded with ID " + arenaId);
    }
}
