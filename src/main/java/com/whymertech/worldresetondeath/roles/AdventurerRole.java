package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AdventurerRole extends GenericRole {

    public double health = 24.0;
    public int speedLevel = 1;

    public AdventurerRole(Player player) {
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
    }

    @Override
    public String name() {
        return "Adventurer";
    }

    public void giveItems() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack shield = new ItemStack(Material.SHIELD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrows = new ItemStack(Material.ARROW, 64);
        
        super.enchantItem(Enchantment.MENDING, 1, sword);
        super.enchantItem(Enchantment.UNBREAKING, 255, sword);
        super.enchantItem(Enchantment.SWEEPING_EDGE, 5, sword);

        super.enchantItem(Enchantment.MENDING, 1, shield);
        super.enchantItem(Enchantment.UNBREAKING, 255, shield);

        super.enchantItem(Enchantment.MENDING, 1, bow);
        super.enchantItem(Enchantment.UNBREAKING, 255, bow);

        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(sword, shield, bow, arrows);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }

    @Override
    public Material favoriteFood() {
        return Material.BROWN_MUSHROOM;
    }

    public void addEffects() {
        super.addSpeedEffect(speedLevel);
    }
}