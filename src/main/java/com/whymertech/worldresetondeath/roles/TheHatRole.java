package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.structure.Structure;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static com.whymertech.worldresetondeath.Utils.plugin;
import static com.whymertech.worldresetondeath.Utils.randInt;

public class TheHatRole extends GenericRole implements Listener {

    public double health = 20.0;
    public Location spawn;
    private static final double xOffset = 1.5;
    private static final double yOffset = 4;
    private static final double zOffset = 3.5;

    private static final String ROCKET_NAME = "Infinite Rockets";

    public TheHatRole(GameManager gameManager) { super(gameManager); }
    public TheHatRole(Player player) {
        super(player);
    }
    
    @Override
    public Location getRoleSpawnLocation() {
        World world = Bukkit.getWorld(GameManager.WORLD_NAME);

        if (world != null) {
            return spawn;
        }
        
        return null;
    }

    @Override
    public void preparePlayer() {
        File skyblockFile = new File(plugin.getDataFolder(), "skyblock.nbt");
        World world = Bukkit.getWorld(GameManager.WORLD_NAME);
        Plugin plugin = (Plugin) Bukkit.getPluginManager().getPlugin("worldresetondeath");

        try {
            Location island = new Location(world, randInt(-1000, 1000), randInt(250, 300), randInt(-1000, 1000));
            plugin.getLogger().info("Skyblock Location Set");
            Structure skyblock = Bukkit.getStructureManager().loadStructure(skyblockFile);
            //Random orientation is cool, but save until i can calculate correct spawn location
//            skyblock.place(island, false, StructureRotation.values()[randInt(0, 3)], Mirror.values()[randInt(0, 2)], 0, 1, new Random());
            double x = xOffset;
            double z = zOffset;
            StructureRotation r = StructureRotation.values()[randInt(0,3)];
            switch (r) {
                case NONE -> {
                    x = xOffset;
                    z = zOffset;
                } case COUNTERCLOCKWISE_90 -> {
                    x = zOffset;
                    z = -xOffset;
                } case CLOCKWISE_90 -> {
                    x = -zOffset;
                    z = xOffset;
                } case CLOCKWISE_180 -> {
                    x = -xOffset;
                    z = -zOffset;
                }
            }
            skyblock.place(island, false, r, Mirror.NONE, 0, 1, new Random());
            spawn = new Location(world, island.getBlockX() + x, island.getBlockY() + yOffset, island.getBlockZ() + z);
            plugin.getLogger().info("Skyblock has been placed");
        } catch (IOException e) {
            giveItems();
            spawn = world.getSpawnLocation();
            plugin.getLogger().severe("Failed to generate skyblock: " + e.getMessage());
        }
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
        giveBaseSword();
        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();
        
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemStack rockets = new ItemStack(Material.FIREWORK_ROCKET, 64);
        ItemMeta rocketMeta = rockets.getItemMeta();
        rocketMeta.setItemName(ROCKET_NAME);
        rockets.setItemMeta(rocketMeta);

        super.enchantItem(Enchantment.MENDING, 1, elytra);
        super.enchantItem(Enchantment.UNBREAKING, 255, elytra);

        super.player.getInventory().addItem(elytra, rockets);
    }

    @Override
    public Material favoriteFood() {
        return Material.CHORUS_FRUIT;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);
        if (event.hasItem()) {
            ItemStack item = event.getItem();
            if (item.getType() == Material.FIREWORK_ROCKET && item.getItemMeta().getItemName().equals(ROCKET_NAME)){
                if (playerRole instanceof TheHatRole) {
                    item.setAmount(32);
                }
                else {
                    //Don't let non-Hats use Infinite Fireworks
                    event.setCancelled(true);
                }
            }

        }
    }

}