package com.nunofacha.chickendefender.listeners;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
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

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e){
        if(e.getEntityType() == EntityType.CHICKEN){
            if(e.getEntity().getName().contains("The Chicken")){
                for (Arena arena :  ArenaManager.getArenas()) {
                    if(arena.getChicken() == e.getEntity()){
                        Main.logger.info("Chicken for arena "+arena.getArenaId()+" has been killed!");
                        arena.sendMessageToAttacking("The chicken is dead, you win!");
                        arena.sendMessageToDefending("The chicken is dead, you lose!");
                        arena.finish();
                    }
                }
            }
        }
    }
}
