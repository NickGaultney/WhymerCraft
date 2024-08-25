package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.attribute.Attribute;

import com.whymertech.worldresetondeath.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import java.util.Random;

public class MinerRole extends GenericRole implements Listener {

    private double health = 18.0;

    public MinerRole(GameManager gameManager) {
        super(gameManager);
    }

    public MinerRole(Player player) {
        super(player);
    }

    @Override
    public Location getRoleSpawnLocation() {
        World world = Bukkit.getWorld(GameManager.WORLD_NAME);

        if (world != null) {
            Location randomLocation = getRandomLocationWithinRadius(world, 25, 100);
            clearArea(randomLocation, 1);
            return randomLocation;
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
        removeMinerEffects();
    }

    @Override
    public String name() {
        return "Miner";
    }

    public void clearArea(Location location, int radius) {
        World world = location.getWorld();
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY; y <= centerY + 2; y++) { // Clear two blocks above the center Y
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.AIR); // Set the block to air
                }
            }
        }
    }

    public Location getRandomLocationWithinRadius(World world, int minDistance, int radius) {
        Random random = new Random();
        Location spawnLocation = world.getSpawnLocation();
    
        // Ensure radius is greater than minDistance
        if (radius < minDistance) {
            throw new IllegalArgumentException("Radius must be greater than minimum distance");
        }
    
        // Calculate random distance and angle
        double distance = minDistance + (random.nextDouble() * (radius - minDistance));
        double angle = random.nextDouble() * 2 * Math.PI;
    
        // Calculate x and z offsets
        int xOffset = (int) (distance * Math.cos(angle));
        int zOffset = (int) (distance * Math.sin(angle));
    
        // Create the new location with y-coordinate set to -40
        int x = spawnLocation.getBlockX() + xOffset;
        int y = 0;
        int z = spawnLocation.getBlockZ() + zOffset;
    
        return new Location(world, x, y, z);
    }
    
    public void giveItems() {
        ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        ItemStack torches = new ItemStack(Material.TORCH, 64);
        ItemStack LaFishO = new ItemStack(Material.SALMON, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 10);
        ItemStack logs = new ItemStack(Material.OAK_LOG, 8);

        ItemMeta pickaxeMeta = diamondPickaxe.getItemMeta();
        if (pickaxeMeta != null) {
            pickaxeMeta.addEnchant(Enchantment.MENDING, 1, true);
            pickaxeMeta.addEnchant(Enchantment.EFFICIENCY, 7, true);
            pickaxeMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
            pickaxeMeta.addEnchant(Enchantment.FORTUNE, 3, true);

            // Use PersistentDataContainer to store custom attributes
            NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    key, 
                    5.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            pickaxeMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            diamondPickaxe.setItemMeta(pickaxeMeta);
        }

        giveBaseAxe();
        giveBaseShovel();
        super.player.getInventory().addItem(diamondPickaxe, torches, LaFishO, bread, logs);
    }

    public void addNightVision() {

        // Apply Speed I effect
        PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, false, false);
        super.player.addPotionEffect(nightVision);
    }

    // TODO use the generic addEffects and removeEffects for all roles 
    public void removeMinerEffects() {
        super.player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public void addEffects() {
        addNightVision();
    }

    public void removeEffects() {
        super.player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public Material favoriteFood() {
        return Material.MELON;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

        // Check if the player has the Farmer role
        if (playerRole instanceof MinerRole) {
            if (event.getItem() == null) return;

            if (event.getItem().getType() == Material.DIAMOND_PICKAXE && event.getAction() == Action.RIGHT_CLICK_AIR) {
                Location originalLocation = player.getLocation();
                player.setGameMode(GameMode.SPECTATOR);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                         player.teleport(originalLocation);
                         player.setGameMode(GameMode.SURVIVAL);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("worldresetondeath"), 300L);
            }
        }
    }
}