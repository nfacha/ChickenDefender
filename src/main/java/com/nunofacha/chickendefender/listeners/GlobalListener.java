package com.nunofacha.chickendefender.listeners;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (Main.arenaManager.isPlaying(e.getPlayer())) {
            Arena arena = Main.arenaManager.getArena(e.getPlayer());
            arena.removePlayer(e.getPlayer());
            arena.sendMessageToAll(e.getPlayer().getName() + " has left the game (disconnected)");
        }
    }
}
