package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;

import java.util.ArrayList;

public class ArenaManager {
    public static ArrayList<Arena> arenas = new ArrayList<>();

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
}
