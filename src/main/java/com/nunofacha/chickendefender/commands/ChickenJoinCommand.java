package com.nunofacha.chickendefender.commands;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChickenJoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /chickenjoin <arena>");
            return false;
        }
        Player p = (Player) sender;
        int arenaId = Integer.parseInt(args[0]);
        Arena arena = Main.arenaManager.getArena(arenaId);
        if (Main.arenaManager.isPlaying(p)) {
            sender.sendMessage("You are already on an arena");
            return false;
        }
        if (!arena.getState().equals(GameState.RECRUITING)) {
            sender.sendMessage("Arena not ready");
            return false;
        }
        arena.addPlayer(p);
        return false;
    }
}
