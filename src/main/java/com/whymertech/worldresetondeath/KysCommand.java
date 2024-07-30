package com.whymertech.worldresetondeath;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KysCommand implements CommandExecutor {
    private Plugin plugin;

    public KysCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kys")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) { // Check if the player is an operator
                    if ( !plugin.worldExists(Plugin.WORLD_NAME)) { // Check if the world exists
                        sender.sendMessage("Kill Yourself");
                        recreateWorlds();
                        plugin.getLogger().info(sender.getName() + " executed /kys command");
                    } else {
                        sender.sendMessage("A hardcore world already exists...use /join to teleport to it");
                    }
                } else {
                    player.sendMessage("You do not have permission to use this command.");
                    plugin.getLogger().info("Unauthorized kys command attempt by: " + player.getName());
                }
            }
            return true;
        }
        return false;
    }


    private void recreateWorlds() {
        Bukkit.broadcastMessage("Creating the overworld...");
        World newWorld = recreateWorld();
        Bukkit.broadcastMessage("Creating the nether...");
        recreateWorldNether();
        Bukkit.broadcastMessage("Creating The End...");
        recreateWorldEnd();

        Bukkit.broadcastMessage("Brace yourselves...");
        // Teleport all players to the new world's spawn location
        teleportPlayersToNewWorld(newWorld);
    }

    private World recreateWorld() {
        plugin.getLogger().info("Attempting to create world...");
        // Create a new world with the specified seed and set the difficulty to hardcore
        plugin.getLogger().info("Creating new world with seed: " + Plugin.SEED);
        WorldCreator creator = new WorldCreator(Plugin.WORLD_NAME);
        creator.seed(Plugin.SEED);
        World newWorld = Bukkit.createWorld(creator);
        newWorld.setDifficulty(Difficulty.HARD);

        plugin.getLogger().info("World has been reset with seed " + Plugin.SEED + " and set to hardcore mode!");

        
        return newWorld;
    }
    
    private void recreateWorldNether() {
        plugin.getLogger().info("Attempting to create world nether...");

        WorldCreator loadWorldNether = new WorldCreator(Plugin.WORLD_NAME + "_nether");
        loadWorldNether.environment(World.Environment.NETHER);
        World newWorld = Bukkit.createWorld(loadWorldNether);
        newWorld.setDifficulty(Difficulty.HARD);

        plugin.getLogger().info("Nether has been created");
    }

    private void recreateWorldEnd() {
        plugin.getLogger().info("Attempting to create the end...");
        
        WorldCreator loadWorldEnd = new WorldCreator(Plugin.WORLD_NAME + "_the_end");
        loadWorldEnd.environment(World.Environment.THE_END);
        World newWorld = Bukkit.createWorld(loadWorldEnd);
        newWorld.setDifficulty(Difficulty.HARD);

        plugin.getLogger().info("The End has been created");
    }

    private void teleportPlayersToNewWorld(World world) {
        plugin.getLogger().info("Teleporting players to the new world's spawn location.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(world.getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL); // Set player game mode to survival
            player.getInventory().clear(); // Clear player inventory
            player.setHealth(20); // Refill health
            player.setFoodLevel(20); // Refill hunger
            player.setSaturation(20); // Refill saturation
        }
    }

}
