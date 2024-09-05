package com.whymertech.worldresetondeath.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.joml.Random;

import com.whymertech.worldresetondeath.GameManager;

public class MobDamageListener implements Listener {

    private GameManager gameManager;

    public MobDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        if (event.getEntity() instanceof Enemy) {
            // Get the current explosion radius
            float currentRadius = event.getYield();

            // Double the explosion radius
            event.setYield(currentRadius * (float) gameManager.mobMultiplier);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (gameManager.mobMultiplier == 1.0) return;
        
        if (event.getDamager() instanceof Enemy) {
            event.setDamage(event.getDamage() * gameManager.mobMultiplier);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (gameManager.mobMultiplier == 1.0) return;

        int deathCount = (int) (gameManager.mobMultiplier / 0.25) - 4;

        // Check if the entity is a hostile mob
        if (event.getEntity() instanceof Enemy) {
            LivingEntity mob = event.getEntity();

            if (mob instanceof Creeper) {
                Creeper creeper = (Creeper) event.getEntity();

                // Explosion radius increase moved to the onEntityExplode event
                //creeper.setExplosionRadius((int) (creeper.getExplosionRadius() * gameManager.mobMultiplier));

                if (gameManager.mobMultiplier <= 4.0) {
                    double tickReduction = deathCount * 2;    // This should come out to 2 ticks less per death. Creepers normally have 30 ticks
                    creeper.setMaxFuseTicks( (int) (creeper.getMaxFuseTicks() - tickReduction));
                } else {
                    creeper.setMaxFuseTicks((creeper.getMaxFuseTicks() - 24));
                }

                if (gameManager.mobMultiplier >= 4.5) {
                    creeper.setPowered(true);
                }

                return;
            }

            if (mob instanceof Zombie || mob instanceof Spider || mob instanceof CaveSpider) {
                AttributeInstance speedAttribute = mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                if (speedAttribute != null) {
                    double speedIncrease = (deathCount * 2) / 100.0; // This should come out to an increase of 0.02 per player death
                    // Set the new speed value (default is 0.23)
                    speedAttribute.setBaseValue(0.23 + speedIncrease); // Example: Increase speed
                }
            }

            if (mob instanceof Spider) {
                chanceSpawnMob(mob, EntityType.CAVE_SPIDER, deathCount * 2);
            }

            if (mob instanceof Enderman) {
                if (mob.getWorld().getEnvironment() == World.Environment.NETHER) {
                    chanceSpawnMob(mob, EntityType.CREEPER, 100);
                } else {
                    chanceSpawnMob(mob, EntityType.CREEPER, deathCount * 2);
                }
            }

            // Increase health by the multiplier
            AttributeInstance healthAttribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (healthAttribute != null) {
                healthAttribute.setBaseValue(healthAttribute.getBaseValue() * gameManager.mobMultiplier);
                mob.setHealth(healthAttribute.getBaseValue()); // Set current health to new max health
            }
        }
    }

    private void chanceSpawnMob(Entity originalMob, EntityType additionalMob, int chance) {
        Random randomObject = new Random();
        int randomNumber = randomObject.nextInt(100);

        if (chance <= 0) return;

        if (randomNumber < chance) {
            Location mobLocation = originalMob.getLocation();

            originalMob.getWorld().spawnEntity(mobLocation, additionalMob);
        }
    }
}
