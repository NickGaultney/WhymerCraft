package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class UndeadRole extends GenericRole implements Listener {

    public double health = 20.0;

    public UndeadRole(GameManager gameManager) {
        super(gameManager);
    }

    public UndeadRole(Player player) {
        super(player);
    }
    
    @Override
    public Location getRoleSpawnLocation() {
        World world = Bukkit.getWorld(GameManager.WORLD_NAME);

        if (world != null) {
            return world.getSpawnLocation();
        }
        
        return null;
    }

    @Override
    public void preparePlayer() {
        giveItems();
    }

    @Override
    public void resetPlayer() {
        super.resetPlayer();
    }

    @Override
    public String name() {
        return "Undead";
    }

    public void giveItems() {
        ItemStack rottenFlesh = new ItemStack(Material.ROTTEN_FLESH, 32);

        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(rottenFlesh);
    }

    // @EventHandler
    // public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
    //     Player player = event.getPlayer();
    //     Role playerRole = gameManager.getRole(player);

    //     if (playerRole == null) return;

    //     // Check if the player has the "Undead" role and cancel sprinting if they do
    //     if (playerRole instanceof UndeadRole) {
    //         event.setCancelled(true);
    //         Bukkit.broadcastMessage("No Sprinting");
    //         player.setSprinting(false);
    //     }
    // }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            Role playerRole = gameManager.getRole(player);

            if (playerRole != null && playerRole instanceof UndeadRole) {
                if (event.getEntity().hasMetadata("attackUndead")) return;

                if (event.getEntity() instanceof Monster) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Role playerRole = gameManager.getRole(player);

            if (playerRole == null) return;

            // Check if the player has the "undead" role
            if (playerRole instanceof UndeadRole) {
                // Get nearby entities within a certain radius
                double radius = 20.0; // Adjust the radius as needed
                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    // Check if the entity is a mob and not already targeting the player
                    if (entity instanceof Mob) {
                        Mob mob = (Mob) entity;
                        if (mob.getTarget() != player) {
                            mob.setTarget(player); // Make the mob hostile towards the player
                            mob.setMetadata("attackUndead", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("worldresetondeath"), true));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

        if (playerRole == null) return;

        // Check if the player has the Undead role
        if (playerRole instanceof UndeadRole) {
            Material food = event.getItem().getType();

            if (food == Material.ROTTEN_FLESH) {
                // If the player is eating Rotten Flesh, remove any hunger debuff
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removePotionEffect(PotionEffectType.HUNGER);
                        player.setSaturation((player.getSaturation() + 12));
                        player.setFoodLevel(player.getFoodLevel() + 4);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("worldresetondeath"), 1L); // Delay to give time for hunger effect to be fully processed
                
            } else {
                // Apply the hunger debuff for any other food
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 4));
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

        if (playerRole == null) return;
        
        // Assuming you have a method to check if a player has the undead role
        if (playerRole instanceof UndeadRole) {
            // Check if it's daytime (0-12300 ticks)
            long time = Bukkit.getWorld(GameManager.WORLD_NAME).getTime();
            boolean isDaytime = time >= 0 && time < 12300;

            // Check if the player is exposed to the sky (light level from sky is high)
            boolean isExposedToSky = player.getLocation().getBlock().getLightFromSky() >= 15;

            if (isDaytime && isExposedToSky) {
                // The player should burn in the sunlight
                player.setFireTicks(20 * 3); // Burn the player for 5 seconds
            }
        }
    }
}