package com.nunofacha.chickendefender.commands;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChickenKitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        Arena arena = Main.arenaManager.getArena(p);
        if (arena == null) {
            sender.sendMessage("You are not in an arena!");
            return false;
        }
        if (arena.getState() == GameState.LIVE) {
            sender.sendMessage("The game has already started");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage("Usage: /chickenkit <kit>");
            return false;
        }
        if (!Main.kits.containsKey(args[0])) {
            sender.sendMessage("Invalid kit!");
            return false;
        }
        if (arena.playerKits.containsKey(p.getUniqueId())) {
            arena.playerKits.remove(p.getUniqueId());
        }
        arena.playerKits.put(p.getUniqueId(), args[0]);
        sender.sendMessage(ChatColor.GREEN + "You have selected the kit: " + args[0]);
        return false;
    }
}
