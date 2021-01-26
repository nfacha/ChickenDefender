package com.nunofacha.chickendefender.arenas.game;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private Arena arena;
    private int seconds;

    public Countdown(Arena arena, int seconds) {
        this.arena = arena;
        this.seconds = seconds;
    }

    public void begin() {
        this.arena.setState(GameState.COUNTDOWN);
        this.runTaskTimer(Main.plugin, 0, 20);
    }

    @Override
    public void run() {
        if (this.seconds == 0) {
            cancel();
            this.
                    arena.start();
        }
        if (seconds % 30 == 0 || seconds <= 10) {
            arena.sendMessageToAll(ChatColor.GREEN + "The game will start in " + seconds + " seconds!");
        }
        if (arena.getPlayers().size() < arena.getMinPlayers()) {
            cancel();
            arena.setState(GameState.RECRUITING);
            Main.logger.info("Start canceled due to insufficient players for arena " + arena.getName());
            arena.sendMessageToAll(ChatColor.RED + "Too few player, countdown canceled while we wait for more players");
        }
        seconds--;
    }
}
