package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

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
    private Location corner1;
    private Location corner2;
    private Vector minVector;
    private Vector maxVector;
    private ArrayList<UUID> players;

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
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.attacking.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.attacking.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.attacking.z"));
        this.defendingSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.defending.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.defending.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.defending.z"));
        this.chickenSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.chicken.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.chicken.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawn.chicken.z"));
        this.corner1 = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.z"));

        int xPos1 = Math.min(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.x"));
        int yPos1 = Math.min(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.y"));
        int zPos1 = Math.min(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.z"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.z"));

        int xPos2 = Math.max(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.x"));
        int yPos2 = Math.max(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.y"));
        int zPos2 = Math.max(Main.plugin.getConfig().getInt(this.configPath + ".locations.corner1.z"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.corner2.z"));

        minVector = new Vector(xPos1, yPos1, zPos1);
        maxVector = new Vector(xPos2, yPos2, zPos2);
        Main.logger.info("Arena " + this.name + " was loaded with ID " + arenaId);
    }

    public boolean containsLocation(Location location) {
        return location.toVector().isInAABB(minVector, maxVector);
    }
}
