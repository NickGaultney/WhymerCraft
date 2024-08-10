package com.whymertech.worldresetondeath.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.whymertech.worldresetondeath.LobbyManager;

public class GiveLobbyItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (sender instanceof Player) {
            Player player = (Player) sender;

            // Create the unique Eye of Ender (lobby item)
            ItemStack lobbyItem = LobbyManager.createLobbyItem();

            // Give the item to the player
            player.getInventory().addItem(lobbyItem);
            player.sendMessage(ChatColor.GREEN + "You have been given the Lobby Item!");

            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            return false;
        }
    }
}
