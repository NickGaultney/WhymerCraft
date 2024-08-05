package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
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
        super.addSpeedEffect(speedLevel);
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
        ItemStack shield = new ItemStack(Material.SHIELD);
        ItemStack bow = new ItemStack(Material.BOW);

        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(shield, bow);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }
}