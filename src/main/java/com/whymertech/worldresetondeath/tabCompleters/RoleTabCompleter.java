package com.whymertech.worldresetondeath.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleTabCompleter implements TabCompleter {

    private final List<String> roles = List.of("Drowned", "TheHat", "Adventurer", "Farmer", "Miner", "Rogue", "Archer", "Fisherman", "Blacksmith", "Enchanter", "Undead"); // Replace with your roles

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) { // If we're completing the first argument
            return getSuggestions(args[0], roles);
        }
        return Collections.emptyList();
    }

    private List<String> getSuggestions(String prefix, List<String> options) {
        List<String> suggestions = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(prefix.toLowerCase())) {
                suggestions.add(option);
            }
        }
        return suggestions;
    }
}
