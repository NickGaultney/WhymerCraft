package com.whymertech.worldresetondeath.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

import java.io.IOException;
//import java.util.Set;
import java.util.UUID;

public class DeathListener implements Listener {

    private final Plugin plugin;
    private GameManager gameManager;

    public DeathListener(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void damage(EntityDamageEvent ev) { //Listens to EntityDamageEvent
        if (ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getEntity();  
            String playerName = player.getName();
            UUID playerUUID = player.getUniqueId();
            if ((player.getHealth() - ev.getFinalDamage()) <= 0) {
                ev.setCancelled(true);
                gameManager.resetPlayer(player);

                // Load the death log file
                YamlConfiguration deathLog = YamlConfiguration.loadConfiguration(plugin.getDeathLogFile());

                // Get the current death count
                int deathCount = deathLog.getInt(playerName + ".deaths", 0);

                // Increment the death count
                deathCount++;

                // Update the death count in the YAML file
                deathLog.set(playerName + ".deaths", deathCount);
                deathLog.set(playerName + ".uuid", playerUUID.toString());

                // Save the updated death log
                try {
                    deathLog.save(plugin.getDeathLogFile());
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to save death log file: " + e.getMessage());
                }

                // Log the death count to the console
                plugin.getLogger().info(playerName + " has died " + deathCount + " times.");

                // Broadcast the death tally to the server
                //broadcastDeathTally(deathLog);
                
                Entity killerEntity = ev.getDamageSource().getCausingEntity();
                String killerName;
                
                if (killerEntity == null) {
                    killerName = "something";
                } else {
                    killerName = ev.getDamageSource().getCausingEntity().getName();
                    if (killerName == null) {
                        killerName = "something";
                    }
                }

                

                Bukkit.broadcastMessage(player.getName() + " was slain by a " + killerName + " for " + ev.getFinalDamage() + " points of damage.");
                gameManager.resetGame();
            }
        }
        
    }

    // private void broadcastDeathTally(YamlConfiguration deathLog) {
    //     StringBuilder message = new StringBuilder("\n*\n*\nDeath Tally:\n");

    //     // Get all player names from the YAML file
    //     Set<String> players = deathLog.getKeys(false);

    //     // Build the broadcast message
    //     for (String player : players) {
    //         int deaths = deathLog.getInt(player + ".deaths");
    //         message.append(player).append(": ").append(deaths).append(" deaths\n");
    //     }

    //     message.append("\n*\n*");
    //     // Broadcast the message to the server
    //     Bukkit.broadcastMessage(message.toString());
    // }

}
