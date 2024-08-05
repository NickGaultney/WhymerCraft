package com.whymertech.worldresetondeath.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.whymertech.worldresetondeath.SeedManager;

public class AddSeedCommand implements CommandExecutor {

    private SeedManager seedManager;
    
    public AddSeedCommand(SeedManager seedManager) {
        this.seedManager = seedManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /addseed <seed> <name>");
            return true;
        }

        String seed = args[0];
        String name = args[1];

        seedManager.addSeed(name, seed);

        sender.sendMessage("You have added the seed: " + seed);
        return true;
    }
}
