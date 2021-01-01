package com.nunofacha.chickendefender.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChickenStartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /chickenstart <arena>");
            return false;
        }
        int arenaId = Integer.parseInt(args[0]);

        return false;
    }
}
