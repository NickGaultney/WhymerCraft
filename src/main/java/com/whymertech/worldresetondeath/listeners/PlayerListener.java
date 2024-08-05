package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import com.whymertech.worldresetondeath.roles.Role;
import com.whymertech.worldresetondeath.roles.BlackSmithRole;

import com.whymertech.worldresetondeath.GameManager;

public class PlayerListener implements Listener {

    private GameManager gameManager;

    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.playerHasJoined(player)) {
            gameManager.resetPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the player has the "blacksmith" role
        Role playerRole = gameManager.getRole(player);
        if (playerRole != null && playerRole instanceof BlackSmithRole) {
            // Check if the item is a sword
            if (item != null && item.getType().toString().endsWith("_SWORD")) {
                // Cancel the event to prevent the use of the sword
                event.setCancelled(true);
                player.sendMessage("Blacksmiths can't use swords...better try an axe.");
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();

            // Check if the player has the "blacksmith" role
            Role playerRole = gameManager.getRole(player);
            if (playerRole != null && playerRole instanceof BlackSmithRole) {
                // Check if the item is a sword
                if (item != null && item.getType().toString().endsWith("_SWORD")) {
                    // Cancel the event to prevent the sword attack
                    event.setCancelled(true);
                    player.sendMessage("The nimble sword fumbles in your beefy grip");
                }
            }
        }
    }


}
