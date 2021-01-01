package com.nunofacha.chickendefender.arenas;

import com.nunofacha.chickendefender.Main;

public class Arena {
    private int arenaId;
    private String name;

    public Arena(int arenaId) {
        this.arenaId = arenaId;
        //Load config
        this.name = Main.plugin.getConfig().getString("arenas." + arenaId + ".name");

        Main.logger.info("Arena " + this.name + " was loaded with ID " + arenaId);
    }
}
