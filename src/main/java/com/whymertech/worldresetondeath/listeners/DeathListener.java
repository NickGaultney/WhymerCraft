package com.whymertech.worldresetondeath.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;
import com.whymertech.worldresetondeath.roles.Role;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
//import java.util.Set;
import java.util.UUID;

public class DeathListener implements Listener {

    private final Plugin plugin;
    private GameManager gameManager;
    private HashSet<UUID> deadPlayers = new HashSet<>();

    public DeathListener(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();

        if (!deadPlayers.isEmpty()) {
            event.setDeathMessage(player.getName() + " you're dead...and so is everyone else.");
            gameOver(player);
            return;
        }

        deadPlayers.add(player.getUniqueId());

        // Prevent the normal death process
        event.setDeathMessage(player.getName() + " has fallen, but their spirit lingers...");

        // Set player to spectator mode
        player.setGameMode(GameMode.SPECTATOR);
        UUID playerUUID = player.getUniqueId();

        Bukkit.broadcastMessage("Their strength grows stronger");
        gameManager.updatePlayerDeaths();

        // Spawn a zombie at world spawn with player's name
        Location spawnLocation = world.getSpawnLocation();
        Zombie playerZombie = world.spawn(spawnLocation, Zombie.class);
        playerZombie.setCustomName(player.getName());
        playerZombie.setCustomNameVisible(true);
        playerZombie.getEquipment().setArmorContents(player.getInventory().getArmorContents());
        playerZombie.setHealth(20.0); // Set zombie's health
        playerZombie.setPersistent(true);
        playerZombie.setMetadata("playerUUID", new FixedMetadataValue(plugin, playerUUID.toString()));
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();

            if (zombie.hasMetadata("playerUUID")) {
                // Check if the potion effect is related to curing (like weakness)
                if (event.getAction() == EntityPotionEffectEvent.Action.ADDED && event.getNewEffect().getType() == PotionEffectType.WEAKNESS) {
                    List<MetadataValue> metadataValues = zombie.getMetadata("playerUUID");
                    for (MetadataValue value : metadataValues) {
                        if (value.getOwningPlugin().equals(plugin)) {
                            String uuidString = value.asString();
                            UUID playerUUID = UUID.fromString(uuidString);
                            Player player = Bukkit.getPlayer(playerUUID);

                            // Use the playerUUID as needed
                            // Start curing process, once completed, allow player to respawn
                            Bukkit.broadcastMessage(player.getName() + " has been revived.");
                            deadPlayers.remove(playerUUID);
                            gameManager.resetPlayerDeathCount();
                            
                            attemptRespawn(player.getName(), zombie.getLocation());

                            zombie.remove();
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();

            if (zombie.hasMetadata("playerUUID")) {
                List<MetadataValue> metadataValues = zombie.getMetadata("playerUUID");
                for (MetadataValue value : metadataValues) {
                    if (value.getOwningPlugin().equals(plugin)) {
                        String uuidString = value.asString();
                        UUID playerUUID = UUID.fromString(uuidString);
                        Player player = Bukkit.getPlayer(playerUUID);
                        Bukkit.broadcastMessage(player.getName() + "\'s soul has been lost. The world shudders...");
                        gameOver(player);
                    }
                }
            }
        }
    }

    private void attemptRespawn(String playerName, Location zombieLocation) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(playerName)) {
                Role playerRole = gameManager.getRole(p);
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(zombieLocation);
                playerRole.addEffects();
                break;
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (gameManager.mobMultiplier == 1.0) return;

        // Check if the entity is a hostile mob
        if (event.getEntity() instanceof Monster) {
            LivingEntity mob = event.getEntity();

            if (mob instanceof Creeper) {
                Creeper creeper = (Creeper) event.getEntity();
                creeper.setExplosionRadius((int) (creeper.getExplosionRadius() * gameManager.mobMultiplier));
                return;
            }

            // Increase health by the multiplier
            AttributeInstance healthAttribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (healthAttribute != null) {
                healthAttribute.setBaseValue(healthAttribute.getBaseValue() * gameManager.mobMultiplier);
                mob.setHealth(healthAttribute.getBaseValue()); // Set current health to new max health
            }

            // Increase damage by the multiplier
            AttributeInstance damageAttribute = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (damageAttribute != null) {
                NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
                // Add a modifier with a unique UUID to avoid stacking
                AttributeModifier damageModifier = new AttributeModifier(
                        key,
                        damageAttribute.getBaseValue() * (gameManager.mobMultiplier - 1), // Increase by the current multiplier minus 1
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.ANY
                );
                damageAttribute.addModifier(damageModifier);
            }
        }
    }

    private void gameOver(Player player) {
        deadPlayers = new HashSet<>();
        gameManager.resetPlayer(player);
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();

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
        
        gameManager.resetGame();
    }

    // @EventHandler
    // public void damage(EntityDamageEvent ev) { //Listens to EntityDamageEvent
    //     if (ev.getEntity() instanceof Player) {
    //         Player player = (Player) ev.getEntity();  
    //         String playerName = player.getName();
    //         UUID playerUUID = player.getUniqueId();
    //         if ((player.getHealth() - ev.getFinalDamage()) <= 0) {
    //             ev.setCancelled(true);
    //             gameManager.resetPlayer(player);

    //             // Load the death log file
    //             YamlConfiguration deathLog = YamlConfiguration.loadConfiguration(plugin.getDeathLogFile());

    //             // Get the current death count
    //             int deathCount = deathLog.getInt(playerName + ".deaths", 0);

    //             // Increment the death count
    //             deathCount++;

    //             // Update the death count in the YAML file
    //             deathLog.set(playerName + ".deaths", deathCount);
    //             deathLog.set(playerName + ".uuid", playerUUID.toString());

    //             // Save the updated death log
    //             try {
    //                 deathLog.save(plugin.getDeathLogFile());
    //             } catch (IOException e) {
    //                 plugin.getLogger().severe("Failed to save death log file: " + e.getMessage());
    //             }

    //             // Log the death count to the console
    //             plugin.getLogger().info(playerName + " has died " + deathCount + " times.");

    //             // Broadcast the death tally to the server
    //             //broadcastDeathTally(deathLog);
                
    //             Entity killerEntity = ev.getDamageSource().getCausingEntity();
    //             String killerName;
                
    //             if (killerEntity == null) {
    //                 killerName = "something";
    //             } else {
    //                 killerName = ev.getDamageSource().getCausingEntity().getName();
    //                 if (killerName == null) {
    //                     killerName = "something";
    //                 }
    //             }

                

    //             Bukkit.broadcastMessage(player.getName() + " was slain by a " + killerName + " for " + ev.getFinalDamage() + " points of damage.");
    //             gameManager.resetGame();
    //         }
    //     }
        
    // }

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
