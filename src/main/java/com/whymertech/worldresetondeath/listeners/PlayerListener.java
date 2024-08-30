package com.whymertech.worldresetondeath.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.roles.Role;

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
}
