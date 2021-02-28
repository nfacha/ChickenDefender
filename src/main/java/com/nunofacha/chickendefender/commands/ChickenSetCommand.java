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
        if (!sender.hasPermission("chickendefender.set")) {
            sender.sendMessage("No permission");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage("Usage: /chickenset lobby/arena");
            return false;
        }
        if (args[0].equals("lobby")) {
            Location location = ((Player) sender).getLocation();
            Main.plugin.getConfig().set("lobby.x", location.getX());
            Main.plugin.getConfig().set("lobby.y", location.getY());
            Main.plugin.getConfig().set("lobby.z", location.getZ());
            Main.plugin.getConfig().set("lobby.world", location.getWorld().getName());
            try {
                Main.plugin.getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                Main.arenaManager.loadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sender.sendMessage("Global Lobby location set");
            return false;
        }
        if (args[0].equals("arena")) {
            if (args.length < 3) {
                sender.sendMessage("Usage: /chickenset arena ID OPTION");
                return false;
            }
            if (args[1].equals("create")) {
                String arenaName = args[2];
                if (Main.arenaManager.getArena(arenaName) != null) {
                    sender.sendMessage("An arena by this name already exists!");
                    return false;
                }
                Main.plugin.getConfig().getConfigurationSection("arenas").createSection(arenaName);
//                Main.plugin.getConfig().set("arenas."+arenaName+".team-selection", false);
                Main.plugin.getConfig().set("arenas." + arenaName + ".countdown", 30);
                Main.plugin.getConfig().set("arenas." + arenaName + ".chicken-health", 50);
                Main.plugin.getConfig().set("arenas." + arenaName + ".default-kit", "demo");
                Main.plugin.getConfig().set("arenas." + arenaName + ".player-glow", true);
                Main.plugin.getConfig().set("arenas." + arenaName + ".team-helmet", false);
                Main.plugin.getConfig().set("arenas." + arenaName + ".clear-inventory", true);
                Main.plugin.getConfig().set("arenas." + arenaName + ".player-lives", 3);
                Main.plugin.getConfig().set("arenas." + arenaName + ".enabled", false);

                try {
                    Main.plugin.getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
//                    arena.loadConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendMessage("The arena arena " + arenaName + " has been created, please use the following commands in order to configure the arena:");
                sender.sendMessage("/chickenset arena " + arenaName + " lobby - Sets the arena lobby\n" +
                        "\n" +
                        "/chickenset arena " + arenaName + " attacking - Sets the arena attacking spawn\n" +
                        "/chickenset arena " + arenaName + " defending - Sets the arena defending spawn\n" +
                        "/chickenset arena " + arenaName + " chicken - Sets the arena chicken spawn\n" +
                        "/chickenset arena " + arenaName + " corner1 - Top right corner of the arena region\n" +
                        "/chickenset arena " + arenaName + " corner2 - Bottom left corner of the arena region\n" +
                        "/chickenset arena " + arenaName + " enable - Enables the arena and makes it ready to be played");
                Arena newArena = new Arena(arenaName);
                ArenaManager.getArenas().add(newArena);
            } else {
                Arena arena = Main.arenaManager.getArena(args[1]);
                if (arena == null) {
                    sender.sendMessage("Invalid arena");
                    return false;
                }
                Location location = ((Player) sender).getLocation();
                switch (args[2]) {
                    case "attacking":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.attacking.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.attacking.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.attacking.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.attacking.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena ATTACKING spawn set");
                        break;
                    case "defending":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.defending.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.defending.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.defending.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena DEFENDING spawn set");
                        break;
                    case "chicken":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.chicken.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.chicken.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.chicken.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena CHICKEN spawn set");
                        break;
                    case "lobby":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.lobby.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.lobby.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.spawns.lobby.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena LOBBY spawn set");
                        break;
                    case "corner1":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner1.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner1.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner1.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena CORNER1 set");
                        break;
                    case "corner2":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner2.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner2.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.corner2.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.world", location.getWorld().getName());
                        sender.sendMessage("Arena CORNER2 set");
                        break;
                    case "sign":
                        location = ((Player) sender).getTargetBlockExact(5).getLocation();
                        if (!location.getBlock().getType().toString().contains("WALL_SIGN")) {
                            sender.sendMessage("You must be looking at a sign (placed on a wall)");
                            return false;
                        }
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.sign.x", location.getX());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.sign.y", location.getY());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.sign.z", location.getZ());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.sign.world", location.getWorld().getName());
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".locations.sign.enabled", true);
                        sender.sendMessage("Arena SIGN set");
                        break;
                    case "enable":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".enabled", true);
                        sender.sendMessage("Arena enabled");
                        break;
                    case "disable":
                        Main.plugin.getConfig().set(arena.getConfigPath() + ".disabled", false);
                        sender.sendMessage("Arena disabled");
                        break;
                }
                try {
                    Main.plugin.getConfig().save(Main.plugin.getDataFolder() + "/config.yml");
                    arena.loadConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
