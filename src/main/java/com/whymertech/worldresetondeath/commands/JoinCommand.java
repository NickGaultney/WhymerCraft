package com.whymertech.worldresetondeath.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

public class JoinCommand implements CommandExecutor {
    private Plugin plugin;
    private GameManager gameManager;

    public JoinCommand(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (gameManager.worldExists(GameManager.WORLD_NAME)) { // Check if the world exists
                    if (!player.getWorld().getName().equals(GameManager.WORLD_NAME)) {
                        if (gameManager.playerHasJoined(player)) {
                            sender.sendMessage("Joining World...");
                            plugin.getLogger().info(sender.getName() + " executed /join command");
                            teleportPlayerToWorld(player);
                        } else {
                            if (gameManager.getRole(player) != null) {
                                sender.sendMessage("Brace yourself.");
                                plugin.getLogger().info(sender.getName() + " executed /join command");
                                gameManager.addPlayer(player);
                            } else {
                                player.sendMessage("You must select a role first.");
                            }
                            
                        }
                    } else {
                        player.sendMessage("You're already in the world...");
                        plugin.getLogger().info("Player attempted to join but is already in the world: " + player.getName());
                    }
                    
                } else {
                    player.sendMessage("Couldn't find a world to join");
                    plugin.getLogger().info("Failed join command attempt by: " + player.getName());
                }
            }
            return true;
        }
        return false;
    }

    private void teleportPlayerToWorld(Player player) {
        World world = Bukkit.getWorld(GameManager.WORLD_NAME);
        
        player.setGameMode(GameMode.SURVIVAL); // Set player game mode to survival
        player.teleport(world.getSpawnLocation());
    }
}
