package com.whymertech.worldresetondeath.listeners;

import com.whymertech.worldresetondeath.GameManager;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.whymertech.worldresetondeath.roles.EnchanterRole;
import com.whymertech.worldresetondeath.roles.Role;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class EnchantmentListener implements Listener {

    GameManager gameManager;
    // Track players who have just enchanted an item
    private HashSet<UUID> recentlyEnchantedPlayers = new HashSet<>();

    public EnchantmentListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();

        // Check if the player has the Enchanter role
        Role playerRole = gameManager.getRole(player);
        if (playerRole != null && playerRole instanceof EnchanterRole) {
            recentlyEnchantedPlayers.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if the player has recently enchanted an item
        if (recentlyEnchantedPlayers.contains(playerUUID)) {
            // Cancel the experience change
            if (event.getNewLevel() < event.getOldLevel()) {
                int levelsLost = event.getOldLevel() - event.getNewLevel();
                // Remove the player from the set after cancelling the experience gain
                player.giveExpLevels(levelsLost);
            } 

            recentlyEnchantedPlayers.remove(playerUUID);
        }
    }
}
