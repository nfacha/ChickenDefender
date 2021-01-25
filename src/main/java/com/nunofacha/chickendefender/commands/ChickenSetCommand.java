package com.nunofacha.chickendefender.commands;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ChickenSetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /chickenset lobby/arena");
            return false;
        }
        if(args[0].equals("lobby")){
            Location location = ((Player) sender).getLocation();
            Main.plugin.getConfig().set("lobby.x", location.getX());
            Main.plugin.getConfig().set("lobby.y", location.getY());
            Main.plugin.getConfig().set("lobby.z", location.getZ());
            Main.plugin.getConfig().set("lobby.world", location.getWorld().getName());
            try {
                Main.plugin.getConfig().save(Main.plugin.getDataFolder()+"/config.yml");
                Main.arenaManager.loadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendMessage("Global Lobby location set");
            return false;
        }
        if(args[0].equals("arena")){
            if (args.length < 3) {
                sender.sendMessage("Usage: /chickenset arena ID OPTION");
                return false;
            }
            int arenaId = Integer.parseInt(args[1]);
            Arena arena = Main.arenaManager.getArena(arenaId);
            if(arena == null){
                sender.sendMessage("Invalid arena");
                return false;
            }
            Location location = ((Player) sender).getLocation();
            switch (args[2]){
                case "attacking":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.attacking.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.attacking.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.attacking.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.attacking.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena ATTACKING spawn set");
                    break;
                case "defending":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.defending.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.defending.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.defending.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena DEFENDING spawn set");
                    break;
                case "chicken":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.chicken.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.chicken.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.chicken.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena CHICKEN spawn set");
                    break;
                case "lobby":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.lobby.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.lobby.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.spawns.lobby.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena LOBBY spawn set");
                    break;
                case "corner1":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner1.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner1.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner1.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena CORNER1 set");
                    break;
                case "corner2":
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner2.x", location.getX());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner2.y", location.getY());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.corner2.z", location.getZ());
                    Main.plugin.getConfig().set(arena.getConfigPath()+".locations.world", location.getWorld().getName());
                    sender.sendMessage("Arena CORNER2 set");
                    break;
            }
            try {
                Main.plugin.getConfig().save(Main.plugin.getDataFolder()+"/config.yml");
                arena.loadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

//        Arena arena = ArenaManager.getArenas().get(Integer.parseInt(args[0]));
//        if(arena == null){
//            sender.sendMessage("Invalid Arena");
//            return false;
//        }
        return false;
    }
}
