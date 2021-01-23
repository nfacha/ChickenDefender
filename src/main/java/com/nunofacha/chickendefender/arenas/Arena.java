package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.game.Countdown;
import com.nunofacha.chickendefender.arenas.game.GameState;
import com.nunofacha.chickendefender.arenas.game.Team;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Location lobbySpawn;
    private Location attackingSpawn;
    private Location defendingSpawn;
    private Location chickenSpawn;
    private Location corner1;
    private Location corner2;
    private Vector minVector;
    private Vector maxVector;
    private ArrayList<UUID> players = new ArrayList<>();
    private GameState state;
    private Countdown countdown;
    private Chicken chicken;
    private ArrayList<UUID> attackingTeam = new ArrayList<>();
    private ArrayList<UUID> defendingTeam = new ArrayList<>();
    public HashMap<UUID, Integer> deathCount = new HashMap<>();

    public Arena(int arenaId) {
        this.arenaId = arenaId;
        this.configPath = "arenas." + arenaId;
        //Load config
        this.name = Main.plugin.getConfig().getString(this.configPath + ".name");
        this.teamSelection = Main.plugin.getConfig().getBoolean(this.configPath + ".team-selection");
        this.minPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.min");
        this.maxPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.max");
        this.world = Main.plugin.getServer().getWorld(Main.plugin.getConfig().getString(this.configPath + ".locations.world"));
        this.lobbySpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.lobby.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.lobby.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.lobby.z"));
        this.attackingSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.attacking.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.attacking.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.attacking.z"));
        this.defendingSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.defending.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.defending.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.defending.z"));
        this.chickenSpawn = new Location(this.world,
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.chicken.x"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.chicken.y"),
                Main.plugin.getConfig().getInt(this.configPath + ".locations.spawns.chicken.z"));
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
        p.teleport(lobbySpawn);
        p.sendMessage("You have been teleported to arena lobby");
        if (players.size() >= getMinPlayers() && !getState().equals(GameState.COUNTDOWN)) {
            countdown.begin();
        }
    }

    public void removePlayer(Player p) {
        if(getTeam(p) == Team.DEFENDING){
            if(defendingTeam.size() == 0){
                sendMessageToAll("All defending players have been eliminated, attacking team wins");
                finish();
            }
        }else{
            if(attackingTeam.size() == 0){
                sendMessageToAll("All attacking players have been eliminated, defending team wins");
                finish();
            }
        }
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
        for (UUID uuid : players) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            // Add the player to a team if they are not yet in one
            if ((!defendingTeam.contains(uuid) && !attackingTeam.contains(uuid))) {
                if (defendingTeam.size() <= attackingTeam.size()) {
                    setPlayerTeam(p, Team.DEFENDING);
                } else {
                    setPlayerTeam(p, Team.ATTACKING);
                }
            }

            //Teleport player to start location
            if (defendingTeam.contains(uuid)) {
                p.teleport(defendingSpawn);
            } else {
                p.teleport(attackingSpawn);
            }
        }
        this.setState(GameState.LIVE);
        //Chicken time

        Entity chickenEntity = chickenSpawn.getWorld().spawnEntity(this.chickenSpawn, EntityType.CHICKEN);
        this.chicken = (Chicken) chickenEntity;
        this.chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
        chicken.setGlowing(true);
        chicken.setHealth(15);
        chicken.setCustomName(ChatColor.BOLD+(ChatColor.RED+"The Chicken"));
        chicken.setCustomNameVisible(true);
    }

    public void finish() {
        for (UUID uuid : players) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            p.teleport(Main.arenaManager.getLobbyLocation());
        }
        players.clear();
        attackingTeam.clear();
        defendingTeam.clear();
        chicken.remove();
        deathCount.clear();
        chicken = null;
        setState(GameState.RECRUITING);

    }

    public void sendMessageToAll(String msg) {
        for (UUID uuid : players) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            p.sendMessage(msg);
        }
    }

    public void sendMessageToAttacking(String msg) {
        for (UUID uuid : attackingTeam) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            p.sendMessage(msg);
        }
    }

    public void sendMessageToDefending(String msg) {
        for (UUID uuid : defendingTeam) {
            Player p = Main.plugin.getServer().getPlayer(uuid);
            p.sendMessage(msg);
        }
    }

    public void setPlayerTeam(Player p, Team team) {
        if (team.equals(Team.ATTACKING)) {
            attackingTeam.add(p.getUniqueId());
            p.sendMessage("You have joined the attacking team");
        } else {
            defendingTeam.add(p.getUniqueId());
            p.sendMessage("You have joined the defending team");
        }
    }


    public int getMinPlayers() {
        return minPlayers;
    }

    public Chicken getChicken() {
        return chicken;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Team getTeam(Player p){
        if(attackingTeam.contains(p.getUniqueId())){
            return Team.ATTACKING;
        }
        if(defendingTeam.contains(p.getUniqueId())){
            return Team.DEFENDING;
        }
        return null;
    }

    public Location getAttackingSpawn() {
        return attackingSpawn;
    }

    public Location getDefendingSpawn() {
        return defendingSpawn;
    }
}
