package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DrownedRole extends GenericRole {

    public double health = 20.0;
    public int speedLevel = 1;

    public DrownedRole(Player player) {
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
        addEffects();
    }

    @Override
    public void resetPlayer() {
        super.resetPlayer();
        removeDrownedEffects();
    }

    @Override
    public String name() {
        return "Drowned";
    }

    public void giveItems() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack trident = new ItemStack(Material.TRIDENT);
        

        super.enchantItem(Enchantment.LOYALTY, 1, trident);
        super.enchantItem(Enchantment.MENDING, 1, trident);
        super.enchantItem(Enchantment.UNBREAKING, 255, trident);

        super.enchantItem(Enchantment.AQUA_AFFINITY, 1, helmet);
        super.enchantItem(Enchantment.MENDING, 1, helmet);
        super.enchantItem(Enchantment.UNBREAKING, 255, helmet);

        super.enchantItem(Enchantment.DEPTH_STRIDER, 1, boots);
        super.enchantItem(Enchantment.MENDING, 1, boots);
        super.enchantItem(Enchantment.UNBREAKING, 255, boots);

        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(trident, helmet, boots);
    }

    public void addBreathingEffect() {
        // Duration is in ticks (20 ticks = 1 second), so this is 1 hour
        int duration = Integer.MAX_VALUE; // Practically infinite duration

        // Apply Speed I effect
        PotionEffect breathingEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, duration, 1, false, false);
        super.player.addPotionEffect(breathingEffect);
    }

    public void addDolphinsGraceEffect() {
        // Duration is in ticks (20 ticks = 1 second), so this is 1 hour
        int duration = Integer.MAX_VALUE; // Practically infinite duration

        // Apply Speed I effect
        PotionEffect dolphinsGrace = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, 1, false, false);
        super.player.addPotionEffect(dolphinsGrace);
    }

    public void addNightVision() {
        // Duration is in ticks (20 ticks = 1 second), so this is 1 hour
        int duration = Integer.MAX_VALUE; // Practically infinite duration

        // Apply Speed I effect
        PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 1, false, false);
        super.player.addPotionEffect(nightVision);
    }

    public void removeDrownedEffects() {
        super.player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        super.player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        super.player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public Material favoriteFood() {
        return Material.SEA_PICKLE;
    }

    public void addEffects() {
        addBreathingEffect();
        addDolphinsGraceEffect();
        addNightVision();
    }
}
