package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;


import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BlackSmithRole extends GenericRole implements Listener{

    public double health = 30.0;
    private HashMap<Material, Material> upgradeableMaterials = new HashMap<>();
    
    public BlackSmithRole(GameManager gameManager) {
        super(gameManager);

        upgradeableMaterials.put(Material.IRON_AXE, Material.DIAMOND_AXE);
        upgradeableMaterials.put(Material.IRON_SWORD, Material.DIAMOND_SWORD);
        upgradeableMaterials.put(Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE);
        upgradeableMaterials.put(Material.IRON_HOE, Material.DIAMOND_HOE);
        upgradeableMaterials.put(Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL);
        upgradeableMaterials.put(Material.IRON_HELMET, Material.DIAMOND_HELMET);
        upgradeableMaterials.put(Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        upgradeableMaterials.put(Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS);
        upgradeableMaterials.put(Material.IRON_BOOTS, Material.DIAMOND_BOOTS);
    }

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
        ItemStack mace = new ItemStack(Material.MACE);
        ItemStack smithing = new ItemStack(Material.SMITHING_TABLE);

        helmet.addEnchantment(Enchantment.FIRE_PROTECTION, 5);
        helmet.addEnchantment(Enchantment.UNBREAKING, 255);

        chestPlate.addEnchantment(Enchantment.FIRE_PROTECTION, 5);
        chestPlate.addEnchantment(Enchantment.UNBREAKING, 255);

        leggings.addEnchantment(Enchantment.FIRE_PROTECTION, 5);
        leggings.addEnchantment(Enchantment.UNBREAKING, 255);

        boots.addEnchantment(Enchantment.FIRE_PROTECTION, 5);
        boots.addEnchantment(Enchantment.UNBREAKING, 255);

        giveBasePickaxe();
        giveBaseAxe();
        giveBaseShovel();

        super.player.getInventory().addItem(mace, helmet, chestPlate, leggings, boots, anvil, smithing);
    }

    @Override
    public Material favoriteFood() {
        return Material.CACTUS;
    }

    public void addEffects() {
        reduceBaseSpeed();
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (player != null) {
            Role playerRole = gameManager.getRole(player);

            if (playerRole != null && playerRole instanceof BlackSmithRole) {
                AnvilInventory anvilInventory = event.getInventory();
                
                // Set the repair cost to 0
                // TODO: This is depreciated. Fix it.
                anvilInventory.setRepairCost(0);
            }
        }
    }

    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent event) {
        // Check if a player is interacting with the Smithing Table
        if (!(event.getView().getPlayer() instanceof Player player)) return;
        Role playerRole = gameManager.getRole(player);

        if (playerRole == null || !(playerRole instanceof BlackSmithRole)) return;

        
        ItemStack template = event.getInventory().getItem(0);
        // If using a smithing template, proceed with vanilla functionality 
        if (template != null) return;


        // Get the input items from the Smithing Table slots
        ItemStack baseItem = event.getInventory().getItem(1);
        ItemStack additionItem = event.getInventory().getItem(2);

        // Check if the items are valid for an upgrade
        if (baseItem == null || additionItem == null) return;

        if (canUpgrade(baseItem, additionItem)) {
            // Create the upgraded item (e.g., Diamond Armor)
            ItemStack upgradedItem = getUpgradedItem(baseItem);

            event.getInventory().setItem(0, new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
            // Set the result of the Smithing Table to the upgraded item
            event.setResult(upgradedItem);
        }
    }

    private boolean canUpgrade(ItemStack baseItem, ItemStack additionItem) {
        // Example: Upgrade Iron Armor/Tools to Diamond with a Diamond
        return (upgradeableMaterials.containsKey(baseItem.getType()) && additionItem.getType() == Material.DIAMOND);
    }

    private ItemStack getUpgradedItem(ItemStack baseItem) {
        // Create the upgraded item based on the base item type
        Material upgradedMaterial = upgradeableMaterials.get(baseItem.getType());
    
        if (upgradedMaterial == null) return null;
    
        // Create the new upgraded item
        ItemStack upgradedItem = new ItemStack(upgradedMaterial);
        ItemMeta baseMeta = baseItem.getItemMeta();
        ItemMeta upgradedMeta = upgradedItem.getItemMeta();
    
        if (baseMeta != null && upgradedMeta != null) {
            // Copy display name from base item
            upgradedMeta.setDisplayName(baseMeta.getDisplayName());
    
            // Transfer all enchantments from base item to upgraded item
            baseMeta.getEnchants().forEach((enchantment, level) -> {
                upgradedMeta.addEnchant(enchantment, level, true);
            });
    
            // Set the updated meta to the upgraded item
            upgradedItem.setItemMeta(upgradedMeta);
        }
    
        return upgradedItem;
    
    }
    
}
