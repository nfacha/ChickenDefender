package com.nunofacha.chickendefender.listeners;

import com.nunofacha.chickendefender.Main;
import com.nunofacha.chickendefender.arenas.Arena;
import com.nunofacha.chickendefender.arenas.ArenaManager;
import com.nunofacha.chickendefender.arenas.game.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GlobalListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (Main.arenaManager.isPlaying(e.getPlayer())) {
            Arena arena = Main.arenaManager.getArena(e.getPlayer());
            arena.removePlayer(e.getPlayer());
            arena.sendMessageToAll("§r§4[-] §r" + e.getPlayer().getName() + " has left the game (disconnected)");
        }
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.CHICKEN) {
            if (e.getEntity().getName().contains("The Chicken")) {
                for (Arena arena : ArenaManager.getArenas()) {
                    if (arena.getChicken() == e.getEntity()) {
                        Main.logger.info("Chicken for arena " + arena.getArenaId() + " has been killed!");
                        arena.sendMessageToAttacking(ChatColor.GREEN + "The chicken is dead, you win!");
                        arena.sendMessageToDefending(ChatColor.RED + "The chicken is dead, you lose!");
                        arena.finish();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (Main.arenaManager.isPlaying(p)) {
            Main.plugin.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
                @Override
                public void run() {
                    p.spigot().respawn();
                    p.setFireTicks(0);
                    Arena arena = Main.arenaManager.getArena(p);
                    if (arena.getTeam(p) == Team.ATTACKING) {
                        arena.sendMessageToDefending(ChatColor.GREEN + "An attacking player has been killed: " + p.getName());
                        arena.sendMessageToAttacking(ChatColor.RED + "A player from your team was killed: " + p.getName());
                    } else {
                        arena.sendMessageToAttacking(ChatColor.GREEN + "An defending player has been killed: " + p.getName());
                        arena.sendMessageToDefending(ChatColor.RED + "A player from your team was killed: " + p.getName());
                    }
                    if (arena.deathCount.containsKey(p.getUniqueId())) {
                        int currentDeaths = arena.deathCount.get(p.getUniqueId());
                        if (currentDeaths >= arena.getPlayerLives()) {
                            arena.sendMessageToAll(ChatColor.RED + (net.md_5.bungee.api.ChatColor.BOLD + p.getName() + " has been eliminated"));
                            arena.removePlayer(p);
                            return;
                        } else {
                            arena.deathCount.replace(p.getUniqueId(), currentDeaths + 1);
                        }
                    } else {
                        arena.deathCount.put(p.getUniqueId(), 1);
                    }
                    if (arena.getTeam(p) == Team.DEFENDING) {
                        p.teleport(arena.getDefendingSpawn());
                    } else {
                        p.teleport(arena.getAttackingSpawn());
                    }
                    e.getDrops().removeIf(itemStack -> true);
                    if (arena.getClearInventory()) {
                        Main.kits.get(arena.playerKits.get(p.getUniqueId())).giveKit(p);
                        if (arena.getTeamHelmet()) {
                            if (arena.getAttackingTeam().contains(p.getUniqueId())) {
                                p.getInventory().setHelmet(new ItemStack(Material.RED_WOOL));
                            } else {
                                p.getInventory().setHelmet(new ItemStack(Material.GREEN_WOOL));
                            }
                        }
                    }
                }
            }, 1);

        }
    }

    @EventHandler
    public void onChickenDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (Main.arenaManager.isPlaying(p)) {
                Arena arena = Main.arenaManager.getArena(p);
                if (arena.getTeam(p) == Team.DEFENDING && e.getEntity() == arena.getChicken()) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You are supposed to defend the chicken, not attack it");
                }

            }
        }
    }

    @EventHandler
    public void onHelmetChange(InventoryClickEvent e) {
        //Slot 39 is the helmet slot
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && e.getSlot() == 39) {
            Player p = (Player) e.getWhoClicked();
            Arena arena = Main.arenaManager.getArena(p);
            if (arena != null) {
                if (arena.getTeamHelmet()) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
