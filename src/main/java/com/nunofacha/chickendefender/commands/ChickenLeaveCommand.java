package com.nunofacha.chickendefender.commands;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChickenLeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        Arena arena = Main.arenaManager.getArena(p);
        if (arena == null) {
            sender.sendMessage("You are not in an arena!");
            return false;
        }
        arena.sendMessageToAll(p.getName() + " has left the game!");
        arena.removePlayer(p);
        return false;
    }
}
