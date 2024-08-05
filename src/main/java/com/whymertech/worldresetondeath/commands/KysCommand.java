package com.whymertech.worldresetondeath.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

public class KysCommand implements CommandExecutor {
    private Plugin plugin;
    private GameManager gameManager;

    public KysCommand(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kys")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) { // Check if the player is an operator
                    if ( !gameManager.worldExists(GameManager.WORLD_NAME)) { // Check if the world exists
                        sender.sendMessage("Kill Yourself");
                        gameManager.resetGame();
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
}
