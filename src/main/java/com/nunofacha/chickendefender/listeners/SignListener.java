package com.nunofacha.chickendefender.listeners;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.game.GameState;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSignClick(PlayerInteractEvent e){
        Block block = e.getClickedBlock();
        if(block.getType().toString().contains("WALL_SIGN")){
            Sign sign = (Sign) block.getState();
            if(sign.getLine(0).contains("CHICKEN-DEF")){
                Arena arena = Main.arenaManager.getArena(Integer.parseInt(sign.getLine(1)));
                if(arena.getSignLocation() == block.getLocation()){
                    Player p = e.getPlayer();
                    if (Main.arenaManager.isPlaying(p)) {
                        p.sendMessage("You are already on an arena");
                        return;
                    }
                    if (!arena.getState().equals(GameState.RECRUITING) && (!arena.getState().equals(GameState.COUNTDOWN) && arena.getPlayers().size() <= arena.getMaxPlayers())) {
                        p.sendMessage("Arena not ready");
                        return;
                    }
                    arena.addPlayer(p);
                }
            }
        }
    }
}
