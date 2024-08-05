package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.event.Listener;
import com.whymertech.worldresetondeath.roles.Role;

public class AnvilListener implements Listener {

    GameManager gameManager;

    public AnvilListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (player != null) {
            Role playerRole = gameManager.getRole(player);

            if (playerRole != null) {
                AnvilInventory anvilInventory = event.getInventory();

                // Set the repair cost to 0
                anvilInventory.setRepairCost(0);
            }
        }
    }
}
