package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ArenaManager {
    private static ArrayList<Arena> arenas = new ArrayList<>();

    public ArenaManager() {
        //Initialize Arenas
        int totalArenas = getArenaCount();
        Main.logger.info("Arena count: " + totalArenas);
        for (int i = 0; i < totalArenas; i++) {
            arenas.add(new Arena(i));
        }
    }

    private int getArenaCount() {
        return Main.plugin.getConfig().getConfigurationSection("arenas").getKeys(false).size();
    }

    public static ArrayList<Arena> getArenas() {
        return arenas;
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

    public Arena getArena(int id) {
        for (Arena arena : arenas) {
            if (arena.getArenaId() == id) {
                return arena;
            }
        }
        return null;
    }
}
