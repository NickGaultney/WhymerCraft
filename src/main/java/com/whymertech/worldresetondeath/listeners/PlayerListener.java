package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        int gameNumber = gameManager.getGameNumber();
        String objective = gameManager.objectiveMaterial.toString();
        double mobMultiplier = gameManager.mobMultiplier;

        String currentZombie = "{\"text\":\"\\nEverybody is alive!\",\"color\":\"green\"}";
        if (gameManager.zombieExists()) {
            currentZombie = "{\"text\":\"\\nOne of your players counts among the dead...\",\"color\":\"red\"}";
        }

        player.performCommand("tellraw @s [\"\"," +
                    "{\"text\":\"--------- \",\"color\":\"yellow\"}," +
                    "{\"text\":\"Welcome\",\"color\":\"white\"}," +
                    "{\"text\":\" ----------------------------\",\"color\":\"yellow\"}," +
                    "{\"text\":\"\\nWelcome to game \"}," +
                    "{\"text\":\"" + gameNumber + "\",\"color\":\"yellow\"}," +
                    "{\"text\":\"! The objective is: \"}," +
                    "{\"text\":\"" + objective + "\",\"color\":\"blue\"}," +
                    "{\"text\":\"\\nThe current mob multiplier is \"}," +
                    "{\"text\":\"" + mobMultiplier + "\",\"color\":\"red\"}," +
                    currentZombie +
                "]");
    }
}
