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

public class LobbyCommand implements CommandExecutor {
    private Plugin plugin;
    private GameManager gameManager;

    public LobbyCommand(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lobby")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (gameManager.worldExists(GameManager.LOBBY_WORLD_NAME)) { // Check if the world exists
                    sender.sendMessage("Joining Lobby...");
                    plugin.getLogger().info(sender.getName() + " executed /join command");
                    teleportPlayerToLobby(player);
                } else {
                    player.sendMessage("Couldn't find the lobby");
                    plugin.getLogger().info("Failed lobby command attempt by: " + player.getName());
                }
            }
            return true;
        }
        return false;
    }

    private void teleportPlayerToLobby(Player player) {
        World world = Bukkit.getWorld(GameManager.LOBBY_WORLD_NAME);
        player.teleport(world.getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE); // Set player game mode to survival
        //player.getInventory().clear(); // Clear player inventor
        //player.setHealth(20); // Refill health
        //player.setFoodLevel(20); // Refill hunger
        //player.setSaturation(20); // Refill saturation
    }    
}