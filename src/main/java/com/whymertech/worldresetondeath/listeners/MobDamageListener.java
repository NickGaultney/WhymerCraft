package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.whymertech.worldresetondeath.GameManager;

public class MobDamageListener implements Listener {

    private GameManager gameManager;

    public MobDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Skeleton) {
                // Increase arrow damage by 25%
                event.setDamage(event.getDamage() * gameManager.mobMultiplier);
            }
        }
    }
}
