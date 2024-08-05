package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

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
        ItemStack rottenFlesh = new ItemStack(Material.ROTTEN_FLESH, 10);

        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(rottenFlesh);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            Role playerRole = gameManager.getRole(player);

            if (playerRole != null && playerRole instanceof UndeadRole) {
                if (event.getEntity() instanceof Monster) {
                    event.setCancelled(true);
                }
            }
        }
    }
}