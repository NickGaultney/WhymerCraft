package com.whymertech.worldresetondeath.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.whymertech.worldresetondeath.GameManager;

public class MobDamageListener implements Listener {

    private GameManager gameManager;

    public MobDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (gameManager.mobMultiplier == 1.0) return;
        
        if (event.getDamager() instanceof Enemy) {
            event.setDamage(event.getDamage() * gameManager.mobMultiplier);
        }

        // if (event.getDamager() instanceof Projectile) {
        //     Arrow arrow = (Arrow) event.getDamager();
        //     if (arrow.getShooter() instanceof Enemy) {
        //         // Increase arrow damage by multiplier amount
        //         event.setDamage(event.getDamage() * gameManager.mobMultiplier);
        //     }
        // }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (gameManager.mobMultiplier == 1.0) return;

        // Check if the entity is a hostile mob
        if (event.getEntity() instanceof Enemy) {
            LivingEntity mob = event.getEntity();

            if (mob instanceof Creeper) {
                Creeper creeper = (Creeper) event.getEntity();
                creeper.setExplosionRadius((int) (creeper.getExplosionRadius() * gameManager.mobMultiplier));

                if (gameManager.mobMultiplier <= 4.0) {
                    double tickReduction = ((gameManager.mobMultiplier / 0.25) - 4) * 2;    // This should come out to 2 ticks less per death. Creepers normally have 30 ticks
                    creeper.setMaxFuseTicks( (int) (creeper.getMaxFuseTicks() - tickReduction));
                } else {
                    creeper.setMaxFuseTicks((creeper.getMaxFuseTicks() - 24));
                }

                if (gameManager.mobMultiplier >= 3.0) {
                    creeper.setPowered(true);
                }

                return;
            }

            // Increase health by the multiplier
            AttributeInstance healthAttribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (healthAttribute != null) {
                healthAttribute.setBaseValue(healthAttribute.getBaseValue() * gameManager.mobMultiplier);
                mob.setHealth(healthAttribute.getBaseValue()); // Set current health to new max health
            }

            // Increase damage by the multiplier
            // AttributeInstance damageAttribute = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            // if (damageAttribute != null) {
            //     NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            //     // Add a modifier with a unique UUID to avoid stacking
            //     AttributeModifier damageModifier = new AttributeModifier(
            //             key,
            //             damageAttribute.getBaseValue() * (gameManager.mobMultiplier - 1), // Increase by the current multiplier minus 1
            //             AttributeModifier.Operation.ADD_NUMBER,
            //             EquipmentSlotGroup.ANY
            //     );
            //     damageAttribute.addModifier(damageModifier);
            // }
        }
    }
}
