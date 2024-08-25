package com.whymertech.worldresetondeath.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

public class ResetPlayerCommand implements CommandExecutor {
    private Plugin plugin;
    private GameManager gameManager;

    public ResetPlayerCommand(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("resetplayer")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) { // Check if the player is an operator
                    String playerName = "";
                    if (args.length > 0) {
                        playerName = args[0];
                    }
                    if (playerName.isEmpty()) playerName = player.getName();
                    Player target = Bukkit.getPlayer(playerName);
                    if (target != null) {
                        gameManager.removeRole(player);
                        gameManager.resetPlayer(target);
                        sender.sendMessage("Resetting " + target.getName());
                        plugin.getLogger().info(sender.getName() + " executed /resetplayer " + playerName);
                    } else {
                        sender.sendMessage("Could not find player: " + playerName);
                    }
                } else {
                    player.sendMessage("You do not have permission to use this command.");
                    plugin.getLogger().info("Unauthorized resetplayer command attempt by: " + player.getName());
                }
            }
            return true;
        }
        return false;
    }
}