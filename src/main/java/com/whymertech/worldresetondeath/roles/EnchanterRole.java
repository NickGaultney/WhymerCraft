package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EnchanterRole extends GenericRole {

    public double health = 20.0;

    public EnchanterRole(Player player) {
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
        super.setMaxHealth(health);
        addEffects();
    }

    @Override
    public void resetPlayer() {
        super.resetPlayer();
        removeWeakness();
    }

    @Override
    public String name() {
        return "Enchanter";
    }

    public void addWeakness() {
        int duration = Integer.MAX_VALUE; // Practically infinite duration

        // Apply Speed I effect
        PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, duration, 0, false, false);
        super.player.addPotionEffect(weakness);
    }

    public void removeWeakness() {
        player.removePotionEffect(PotionEffectType.WEAKNESS);
    }

    public void giveItems() {
        ItemStack bookShelves = new ItemStack(Material.BOOKSHELF, 15);

        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(bookShelves);
    }

    @Override
    public Material favoriteFood() {
        return Material.COCOA_BEANS;
    }

    public void addEffects() {
        addWeakness();
    }
}