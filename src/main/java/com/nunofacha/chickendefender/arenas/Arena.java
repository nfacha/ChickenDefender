package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.game.Countdown;
import com.nunofacha.chickendefender.arenas.game.GameState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
    private GameState state;
    private Countdown countdown;

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
        state = GameState.RECRUITING;
        countdown = new Countdown(this, 30);
        Main.logger.info("Arena " + this.name + " was loaded with ID " + arenaId);
    }

    public boolean containsLocation(Location location) {
        return location.toVector().isInAABB(minVector, maxVector);
    }

    public int getArenaId() {
        return arenaId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        players.add(p.getUniqueId());
        p.teleport(Main.arenaManager.getLobbyLocation());
        p.sendMessage("You have been teleported to arena lobby");
        if (players.size() >= getMinPlayers()) {
            countdown.begin();
        }
    }

    public void removePlayer(Player p) {
        players.remove(p.getUniqueId());
        p.teleport(Main.arenaManager.getLobbyLocation());
        p.sendMessage("You left the arena");
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void start() {

    }

    public void sendMessageToAll(String msg) {
        for (UUID uuid : players) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            p.sendMessage(msg);
        }
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTeamSelection() {
        return teamSelection;
    }

    public void setTeamSelection(boolean teamSelection) {
        this.teamSelection = teamSelection;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Location getAttackingSpawn() {
        return attackingSpawn;
    }

    public void setAttackingSpawn(Location attackingSpawn) {
        this.attackingSpawn = attackingSpawn;
    }

    public Location getDefendingSpawn() {
        return defendingSpawn;
    }

    public void setDefendingSpawn(Location defendingSpawn) {
        this.defendingSpawn = defendingSpawn;
    }

    public Location getChickenSpawn() {
        return chickenSpawn;
    }

    public void setChickenSpawn(Location chickenSpawn) {
        this.chickenSpawn = chickenSpawn;
    }

    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public Vector getMinVector() {
        return minVector;
    }

    public void setMinVector(Vector minVector) {
        this.minVector = minVector;
    }

    public Vector getMaxVector() {
        return maxVector;
    }

    public void setMaxVector(Vector maxVector) {
        this.maxVector = maxVector;
    }

    public void setPlayers(ArrayList<UUID> players) {
        this.players = players;
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }
}
