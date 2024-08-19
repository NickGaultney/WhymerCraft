package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Random;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FisherManRole extends GenericRole implements Listener {

    public double health = 20.0;
    public int speedLevel = 1;

    public FisherManRole(GameManager gameManager) {
        super(gameManager);
    }

    public FisherManRole(Player player) {
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
    }

    @Override
    public String name() {
        return "Fisherman";
    }

    public void giveItems() {
        ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
        ItemStack boat = new ItemStack(Material.BIRCH_BOAT);

        ItemMeta fishingRodMeta = fishingRod.getItemMeta();
        if (fishingRodMeta != null) {
            fishingRodMeta.addEnchant(Enchantment.MENDING, 1, true);
            fishingRodMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 7, true);
            fishingRodMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
            fishingRodMeta.addEnchant(Enchantment.LURE, 5, true);

            // Use PersistentDataContainer to store custom attributes
            NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    key, 
                    6.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            fishingRodMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            fishingRod.setItemMeta(fishingRodMeta);
        }

        giveBaseAxe();
        giveBasePickaxe();
        giveBaseShovel();
        super.player.getInventory().addItem(fishingRod, boat);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Check if the player caught an item
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            Role playRole = super.gameManager.getRole(player);
            // Check if the player has the fisherman role
            if (playRole instanceof FisherManRole) {
                Random random = new Random();
                int chance = random.nextInt(1000); // 0 to 99

                // Get the caught item
                Item caught = (Item) event.getCaught();
                ItemStack newItem;

                Bukkit.getPluginManager().getPlugin("worldresetondeath").getLogger().info("Fishing chance: " + chance);
                if (chance < 10) {
                    newItem = new ItemStack(Material.BLAZE_ROD);
                } else if (chance < 15) {
                    newItem = new ItemStack(Material.NETHER_WART);
                } else if (chance < 25) {
                    newItem = new ItemStack(Material.ENDER_PEARL);
                } else if (chance < 30) {
                        newItem = new ItemStack(Material.SPAWNER);
                } else if (chance == 995) {
                    newItem = new ItemStack(Material.SKELETON_SPAWN_EGG);
                } else if (chance == 996) {
                    newItem = new ItemStack(Material.IRON_GOLEM_SPAWN_EGG);
                } else if (chance == 997) {
                    newItem = new ItemStack(Material.VILLAGER_SPAWN_EGG);
                } else if (chance == 998) {
                    newItem = new ItemStack(Material.COW_SPAWN_EGG);
                } else if (chance == 999) {
                    newItem = new ItemStack(Material.ANCIENT_DEBRIS);
                } else {
                    return;
                }

                // Replace the original item with the new item
                if (newItem != null) {
                    caught.setItemStack(newItem);
                }
            }
        }
    }
}
