package com.whymertech.worldresetondeath;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private Plugin plugin;

    public JoinCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (plugin.worldExists(Plugin.WORLD_NAME)) { // Check if the world exists
                    if (!player.getWorld().getName().equals(Plugin.WORLD_NAME)) {
                        sender.sendMessage("Joining World...");
                        plugin.getLogger().info(sender.getName() + " executed /join command");
                        plugin.teleportPlayerToWorld(player);
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
}
