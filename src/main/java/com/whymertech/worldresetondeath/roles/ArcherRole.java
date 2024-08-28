package com.whymertech.worldresetondeath.roles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.whymertech.worldresetondeath.GameManager;

public class ArcherRole extends GenericRole implements Listener{
    public double health = 16.0;
    public int speedLevel = 2;

    public ArcherRole(GameManager gameManager) {
        super(gameManager);
    }

    public ArcherRole(Player player) {
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
        return "Archer";
    }

    public void giveItems() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        super.enchantItem(Enchantment.INFINITY, 1, bow);
        super.enchantItem(Enchantment.POWER, 5, bow);
        super.enchantItem(Enchantment.UNBREAKING, 255, bow);
        super.enchantItem(Enchantment.MENDING, 1, bow);

        super.enchantItem(Enchantment.FEATHER_FALLING, 8, boots);
        super.enchantItem(Enchantment.UNBREAKING, 255, boots);
        super.enchantItem(Enchantment.MENDING, 1, boots);

        giveBaseAxe();
        giveBasePickaxe();
        giveBaseShovel();
        super.player.getInventory().addItem(bow, helmet, chestPlate, leggings, boots, arrow);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }

    @Override
    public Material favoriteFood() {
        return Material.WHEAT;
    }

    public void addEffects() {
        super.addSpeedEffect(speedLevel);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow) {
            ProjectileSource shooter = arrow.getShooter();

            // Check if the shooter is a player
            if (!(shooter instanceof Player)) return;
            
            Player player = (Player) shooter;
            Role playerRole = gameManager.getRole(player);

            if (playerRole == null || !(playerRole instanceof ArcherRole)) return;

            // Scale the damage
            double newDmg = event.getDamage() * gameManager.mobMultiplier;
            player.sendMessage("Damage: " + newDmg);
            event.setDamage(event.getDamage() * gameManager.mobMultiplier);

        }
    }
}
