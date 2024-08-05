package com.whymertech.worldresetondeath.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.roles.Role;

public class RoleCommand implements CommandExecutor {

    private final GameManager gameManager;
    
    public RoleCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can choose roles!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Usage: /role <roleName>");
            return true;
        }

        String roleName = args[0];
        Role role;

        role = gameManager.getRoleFromName(player, roleName);

        gameManager.setRole(player, role);
        player.sendMessage("You have chosen the role: " + role.name());
        return true;
    }
}
