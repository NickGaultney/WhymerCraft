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

public class BlackSmithRole extends GenericRole {

    public double health = 30.0;

    public BlackSmithRole(Player player) {
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
        removeSlowness();
    }

    @Override
    public String name() {
        return "BlackSmith";
    }

    public void reduceBaseSpeed() {

        // Apply Speed I effect
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 0, false, false);
        super.player.addPotionEffect(slowness);
    }

    public void removeSlowness() {
        player.removePotionEffect(PotionEffectType.SLOWNESS);
    }

    public void giveItems() {
        ItemStack anvil = new ItemStack(Material.ANVIL, 1);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(helmet, chestPlate, leggings, boots, anvil);
    }

    public void addEffects() {
        reduceBaseSpeed();
    }
}
