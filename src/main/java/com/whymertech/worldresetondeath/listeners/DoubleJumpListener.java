package com.whymertech.worldresetondeath.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.roles.Role;
import com.whymertech.worldresetondeath.Plugin;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class DoubleJumpListener implements Listener {

    private GameManager gameManager;
    private Plugin plugin;
    private boolean delay = false;

    public DoubleJumpListener(Plugin plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

         if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            if (playerRole != null && (playerRole.canDoubleJump())) {
                if (player.getVelocity().getY() < -0.08) {
                    player.setAllowFlight(false);
                    //player.sendMessage("Flight disabled");
                    delay = true;
                } else {
                    if (!delay) {
                        player.setAllowFlight(true);
                        //player.sendMessage("Flight enabled");
                    } else {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                delay = false;
                            }
                        }.runTaskLater(plugin, 1L); // Delay to give time for jump event to be fully processed
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

         // If the player is not on the ground (i.e., they're trying to double jump)
        if (playerRole != null && (playerRole.canDoubleJump())) {
            //player.sendMessage("You are in fact not touching the ground.");
            // Cancel the flight toggle (to prevent flying)
            event.setCancelled(true);

            // Disable flight so they can't fly again until they touch the ground
            player.setAllowFlight(false);

            // Set the player's velocity upwards to simulate a jump
            player.setVelocity(player.getVelocity().setY(0.5));

            // Play a sound or particle effect (optional)
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.player.attack.strong", 1.0f, 1.0f);
        } 
    }
}
