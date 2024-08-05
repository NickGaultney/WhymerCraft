package com.whymertech.worldresetondeath.roles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GenericRole implements Role {

    public Player player;

    public GenericRole(Player player) {
        this.player = player;
    }

    @Override
    public Location getRoleSpawnLocation() { 
        return null;
    }

    @Override
    public void preparePlayer() {
    }

    @Override
    public void resetPlayer() {
        resetMaxHealth();
        resetSpeed();
    }

    @Override
    public String name() {
        return "Generic";
    }

    public void giveBaseAxe() {
        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);

        ItemMeta axeMeta = ironAxe.getItemMeta();
        if (axeMeta != null) {
            axeMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
            axeMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        }
        ironAxe.setItemMeta(axeMeta);

        player.getInventory().addItem(ironAxe);
    }

    public void giveBaseSword() {
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        
        player.getInventory().addItem(stoneSword);
    }

    public void giveBasePickaxe() {
        ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        ItemMeta pickaxeMeta = diamondPickaxe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.MENDING, 1, true);
        pickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        pickaxeMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        diamondPickaxe.setItemMeta(pickaxeMeta);

        player.getInventory().addItem(diamondPickaxe);
    }

    public void giveBaseShovel() {
        ItemStack ironShovel = new ItemStack(Material.IRON_SHOVEL);

        ItemMeta shovelMeta = ironShovel.getItemMeta();
        if (shovelMeta != null) {
            shovelMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
            shovelMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        }
        ironShovel.setItemMeta(shovelMeta);

        player.getInventory().addItem(ironShovel);
    }

    // Method to set a player's max health
    public void setMaxHealth(double health) {
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (healthAttribute != null) {
            // Remove existing modifiers to avoid stacking effects
            for (AttributeModifier modifier : healthAttribute.getModifiers()) {
                healthAttribute.removeModifier(modifier);
            }

            // Set the new max health
            healthAttribute.setBaseValue(health);
        }
    }

    public void addSpeedEffect(int speedModifier) {
        // Duration is in ticks (20 ticks = 1 second), so this is 1 hour
        int duration = Integer.MAX_VALUE; // Practically infinite duration

        // Apply Speed I effect
        PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, duration, speedModifier, false, false);
        player.addPotionEffect(speedEffect);
    }
    

    // Method to reset a player's max health to default
    public void resetMaxHealth() {
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (healthAttribute != null) {
            // Remove existing modifiers to reset the health to its base value
            for (AttributeModifier modifier : healthAttribute.getModifiers()) {
                healthAttribute.removeModifier(modifier);
            }

            // Optionally, set the base value to default (20.0 is the default max health in Minecraft)
            healthAttribute.setBaseValue(20.0);
        }
    }

    // Method to reset a player's max health to default
    public void resetSpeed() {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public boolean canDoubleJump() {
        return false;
    }

    public void enchantItem(Enchantment enchantment, int level, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
        }

        item.setItemMeta(itemMeta);
    }
}
