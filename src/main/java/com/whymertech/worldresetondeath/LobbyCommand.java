package com.whymertech.worldresetondeath;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    private Plugin plugin;

    public LobbyCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lobby")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (plugin.worldExists(Plugin.TEMP_WORLD_NAME)) { // Check if the world exists
                    sender.sendMessage("Joining Lobby...");
                    plugin.getLogger().info(sender.getName() + " executed /join command");
                    plugin.teleportPlayerToLobby(player);
                } else {
                    player.sendMessage("Couldn't find the lobby");
                    plugin.getLogger().info("Failed lobby command attempt by: " + player.getName());
                }
            }
            return true;
        }
        return false;
    }
}