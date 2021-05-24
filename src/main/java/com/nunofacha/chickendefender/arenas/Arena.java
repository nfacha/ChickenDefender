package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.game.Countdown;
import com.nunofacha.chickendefender.arenas.game.GameState;
import com.nunofacha.chickendefender.arenas.game.Team;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions"})
public class Arena {
    private final String arenaId;
    private final String configPath;
    public HashMap<UUID, Integer> deathCount = new HashMap<>();
    public HashMap<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private boolean teamSelection;
    private int minPlayers;
    private int maxPlayers;
    private World world;
    private Location lobbySpawn;
    private Location attackingSpawn;
    private Location defendingSpawn;
    private Location chickenSpawn;
    private Location signLocation;
    private Boolean signEnabled;
    private Location corner1;
    private Location corner2;
    private Vector minVector;
    private Vector maxVector;
    private ArrayList<UUID> players = new ArrayList<>();
    private GameState state;
    private Countdown countdown;
    private int countdownDuration;
    private int chickenHealth;
    private int playerLives;
    private Chicken chicken;
    private ArrayList<UUID> attackingTeam = new ArrayList<>();
    private ArrayList<UUID> defendingTeam = new ArrayList<>();
    public HashMap<UUID, String> playerKits = new HashMap<>();
    private String defaultKit;
    private Boolean clearInventory;
    private Boolean teamHelmet;
    private Boolean playerGlow;
    private Boolean friendlyFire;
    private Boolean enabled;

    public Arena(String arenaId) {
        this.arenaId = arenaId;
        this.configPath = "arenas." + arenaId;
        //Load config
        loadConfig();
        state = GameState.RECRUITING;
    }

    public void loadConfig() {
        //Load config
        this.enabled = Main.plugin.getConfig().getBoolean(this.configPath + ".enabled");
        if (!this.enabled) {
            Main.logger.info("Arena " + getArenaId() + " is disabled, canceling arena load");
            return;
        }
        this.defaultKit = Main.plugin.getConfig().getString(this.configPath + ".default-kit");
        this.teamSelection = Main.plugin.getConfig().getBoolean(this.configPath + ".team-selection");
        this.minPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.min");
        this.maxPlayers = Main.plugin.getConfig().getInt(this.configPath + ".players.max");
        this.countdownDuration = Main.plugin.getConfig().getInt(this.configPath + ".countdown");
        this.chickenHealth = Main.plugin.getConfig().getInt(this.configPath + ".chicken-health");
        this.playerLives = Main.plugin.getConfig().getInt(this.configPath + ".player-lives");
        this.signEnabled = Main.plugin.getConfig().getBoolean(this.configPath + ".locations.sign.enabled");
        this.clearInventory = Main.plugin.getConfig().getBoolean(this.configPath + ".clear-inventory");
        this.teamHelmet = Main.plugin.getConfig().getBoolean(this.configPath + ".team-helmet");
        this.playerGlow = Main.plugin.getConfig().getBoolean(this.configPath + ".player-glow");
        this.friendlyFire = Main.plugin.getConfig().getBoolean(this.configPath + ".friendly-fire");
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
        if (Main.plugin.getConfig().getString(this.configPath + ".locations.sign.world") == null) {
            this.signLocation = null;
        } else {
            this.signLocation = new Location(
                    Main.plugin.getServer().getWorld(Main.plugin.getConfig().getString(this.configPath + ".locations.sign.world")),
                    Main.plugin.getConfig().getInt(this.configPath + ".locations.sign.x"),
                    Main.plugin.getConfig().getInt(this.configPath + ".locations.sign.y"),
                    Main.plugin.getConfig().getInt(this.configPath + ".locations.sign.z"));
        }

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
        Main.logger.info("Arena " + this.arenaId + " was loaded with ID " + arenaId);
        updateSign();
    }

    public boolean containsLocation(Location location) {
        return location.toVector().isInAABB(minVector, maxVector);
    }

    public String getArenaId() {
        return arenaId;
    }


    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        players.add(p.getUniqueId());
        p.teleport(lobbySpawn);
        p.sendMessage("You have been teleported to arena lobby");
        sendMessageToAll("§r§a[+] §r " + p.getName());
        if (players.size() >= getMinPlayers() && !getState().equals(GameState.COUNTDOWN)) {
            countdown = new Countdown(this, countdownDuration);
            countdown.begin();
        }
        if (clearInventory) {
            playerInventory.put(p.getUniqueId(), p.getInventory().getContents());
            Main.logger.info("Saved inventory of " + p.getName());
            p.getInventory().clear();
            playerKits.put(p.getUniqueId(), defaultKit);
            p.sendMessage(ChatColor.YELLOW + "You have been assigned the kit " + defaultKit);
            p.sendMessage(ChatColor.YELLOW + "You can change your kit with /chickenkit <name>");
            p.sendMessage(ChatColor.YELLOW + "The following kits are available:");
            for (String kitName : Main.kits.keySet()) {
                p.sendMessage(ChatColor.GOLD + "-> " + kitName);
            }
        }
        updateSign();

    }

    public void removePlayer(Player p) {
        removePlayer(p, null);
    }

    public void removePlayer(Player p, Iterator<UUID> iterator) {
        if (clearInventory) {
            p.getInventory().clear();
            if (playerInventory.containsKey(p.getUniqueId())) {
                Main.logger.info("Returning inventory of " + p.getName());
                p.getInventory().setContents(playerInventory.get(p.getUniqueId()));
            }
            playerInventory.remove(p.getUniqueId());
            playerKits.remove(p.getUniqueId());
        }
        if (getTeam(p) == Team.DEFENDING) {
            defendingTeam.remove(p.getUniqueId());
            if (iterator == null) {
                players.remove(p.getUniqueId());
            } else {
                iterator.remove();
            }

            if (defendingTeam.size() == 0) {
                sendMessageToAll("All defending players have been eliminated, attacking team wins");
                finish();
            }
        } else {
            attackingTeam.remove(p.getUniqueId());
            if (iterator == null) {
                players.remove(p.getUniqueId());
            } else {
                iterator.remove();
            }

            if (attackingTeam.size() == 0) {
                sendMessageToAll("All attacking players have been eliminated, defending team wins");
                finish();
            }
        }
        if (playerGlow) {
            Main.sbDefendTeam.removeEntry(p.getName());
            Main.sbAttackTeam.removeEntry(p.getName());
            p.removePotionEffect(PotionEffectType.GLOWING);
        }
        p.teleport(Main.arenaManager.getLobbyLocation());
        p.sendMessage("You left the arena");
        sendMessageToAll("§r§4[-] §r " + p.getName());
        updateSign();
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
//                    setPlayerTeam(p, Team.ATTACKING);
                } else {
                    setPlayerTeam(p, Team.ATTACKING);
//                    setPlayerTeam(p, Team.DEFENDING);
                }
            }

            //Teleport player to start location
            if (defendingTeam.contains(uuid)) {
                p.teleport(defendingSpawn);
            } else {
                p.teleport(attackingSpawn);
            }
            p.setScoreboard(Main.scoreboard);
            if (clearInventory) {
                Main.kits.get(playerKits.get(p.getUniqueId())).giveKit(p);
                if (teamHelmet) {
                    if (attackingTeam.contains(p.getUniqueId())) {
                        p.getInventory().setHelmet(new ItemStack(Material.RED_WOOL));
                    } else {
                        p.getInventory().setHelmet(new ItemStack(Material.GREEN_WOOL));
                    }
                }
            }
        }
        this.setState(GameState.LIVE);
        //Chicken time

        Entity chickenEntity = chickenSpawn.getWorld().spawnEntity(this.chickenSpawn, EntityType.CHICKEN);
        this.chicken = (Chicken) chickenEntity;
        this.chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.chickenHealth);
        chicken.setGlowing(true);
        chicken.setHealth(this.chickenHealth);
        chicken.setCustomName(ChatColor.BOLD + (ChatColor.RED + "The Chicken"));
        chicken.setCustomNameVisible(true);
        updateSign();
    }

    public void finish() {
        Iterator<UUID> iterator = players.iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Player p = Main.plugin.getServer().getPlayer(uuid);
            removePlayer(p, iterator);
        }
        players.clear();
        attackingTeam.clear();
        defendingTeam.clear();
        playerKits.clear();
        playerInventory.clear();
        if (chicken != null) {
            chicken.remove();
        }
        deathCount.clear();
        chicken = null;
        setState(GameState.RECRUITING);
        updateSign();

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
            p.sendMessage(ChatColor.RED + "You have joined the attacking team");
            if (playerGlow) {
                Main.sbAttackTeam.addEntry(p.getName());
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true));
            }
        } else {
            defendingTeam.add(p.getUniqueId());
            p.sendMessage(ChatColor.GREEN + "You have joined the defending team");
            if (playerGlow) {
                Main.sbDefendTeam.addEntry(p.getName());
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true));
            }
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

    public Team getTeam(Player p) {
        if (attackingTeam.contains(p.getUniqueId())) {
            return Team.ATTACKING;
        }
        if (defendingTeam.contains(p.getUniqueId())) {
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

    public int getPlayerLives() {
        return playerLives;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void updateSign() {
        if (signEnabled) {
            Block block = signLocation.getBlock();
            if (!block.getType().toString().contains("WALL_SIGN")) {
                Main.logger.severe("Unable to set arena sign, invalid block for arena " + arenaId + " expected sign found " + block.getType().toString());
                return;
            }
            Sign sign = (Sign) block.getState();
            sign.setLine(0, ChatColor.GOLD + ("CHICKEN-DEF"));
            sign.setLine(1, arenaId + "");
            switch (state) {
                case RECRUITING:
                    sign.setLine(2, ChatColor.GREEN + (ChatColor.BOLD + "RECRUITING"));
                    break;
                case COUNTDOWN:
                    sign.setLine(2, ChatColor.YELLOW + (ChatColor.BOLD + "STARTING..."));
                    break;
                case LIVE:
                    sign.setLine(2, ChatColor.RED + (ChatColor.BOLD + "IN GAME"));
                    break;
            }
            sign.setLine(3, players.size() + "/" + maxPlayers);
            sign.update();
        }
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public Boolean getClearInventory() {
        return clearInventory;
    }

    public ArrayList<UUID> getAttackingTeam() {
        return attackingTeam;
    }

    public ArrayList<UUID> getDefendingTeam() {
        return defendingTeam;
    }

    public Boolean getTeamHelmet() {
        return teamHelmet;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(Boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }
}
