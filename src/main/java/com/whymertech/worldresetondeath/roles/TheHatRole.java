package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TheHatRole extends GenericRole {

    public double health = 20.0;

    public TheHatRole(Player player) {
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
        return "TheHat";
    }

    public void giveItems() {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemStack rockets = new ItemStack(Material.FIREWORK_ROCKET, 64);

        super.enchantItem(Enchantment.MENDING, 1, elytra);
        super.enchantItem(Enchantment.UNBREAKING, 255, elytra);

        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(elytra, rockets);
    }
}