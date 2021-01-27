package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class ArenaManager {
    private static ArrayList<Arena> arenas = new ArrayList<>();
    private Location lobbyLocation;

    public ArenaManager() {
        //Initialize Arenas
        int totalArenas = getArenaCount();
        Main.logger.info("Arena count: " + totalArenas);
        Set<String> arenaNames = Main.plugin.getConfig().getConfigurationSection("arenas").getKeys(false);
        for (String arena : arenaNames) {
            arenas.add(new Arena(String.valueOf(arena)));
        }
        loadConfig();
    }

    public static ArrayList<Arena> getArenas() {
        return arenas;
    }

    public void loadConfig() {
        //noinspection ConstantConditions
        this.lobbyLocation = new Location(
                Main.plugin.getServer().getWorld(Main.plugin.getConfig().getString("lobby.world")),
                Main.plugin.getConfig().getInt("lobby.x"),
                Main.plugin.getConfig().getInt("lobby.y"),
                Main.plugin.getConfig().getInt("lobby.z")
        );
    }

    private int getArenaCount() {
        return Main.plugin.getConfig().getConfigurationSection("arenas").getKeys(false).size();
    }

    public boolean isPlaying(Player p) {
        for (Arena arena : arenas) {
            if (arena.getPlayers().contains(p.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public Arena getArena(Player p) {
        for (Arena arena : arenas) {
            if (arena.getPlayers().contains(p.getUniqueId())) {
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(String id) {
        for (Arena arena : arenas) {
            if (arena.getArenaId().equals(id)) {
                return arena;
            }
        }
        return null;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }


}
