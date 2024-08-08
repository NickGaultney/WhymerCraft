package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FarmerRole extends GenericRole {

    public FarmerRole(Player player) {
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
        return "Farmer";
    }
    
    public void giveItems() {
        ItemStack diamondHoe = new ItemStack(Material.DIAMOND_HOE);
        ItemStack diamondShovel = new ItemStack(Material.DIAMOND_SHOVEL);

        ItemStack potatoes = new ItemStack(Material.POTATO, 1);
        ItemStack carrots = new ItemStack(Material.CARROT, 1);
        ItemStack seeds = new ItemStack(Material.WHEAT_SEEDS, 1);
        ItemStack melon_seeds = new ItemStack(Material.MELON_SEEDS, 1);
        ItemStack pumpkin_seeds = new ItemStack(Material.PUMPKIN_SEEDS, 1);
        ItemStack beetroot_seeds = new ItemStack(Material.BEETROOT_SEEDS, 1);
        ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE, 1);
        ItemStack cactus = new ItemStack(Material.CACTUS, 1);
        
        ItemMeta hoeMeta = diamondHoe.getItemMeta();
        if (hoeMeta != null) {
            hoeMeta.addEnchant(Enchantment.MENDING, 1, true);
            hoeMeta.addEnchant(Enchantment.FORTUNE, 3, true);
            hoeMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
            hoeMeta.addEnchant(Enchantment.UNBREAKING, 255, true);

            // Use PersistentDataContainer to store custom attributes
            NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    key, 
                    6.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            hoeMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            // Optionally, hide attributes from the item lore
            //hoeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            diamondHoe.setItemMeta(hoeMeta);
        }

        ItemMeta shovelMeta = diamondShovel.getItemMeta();
        shovelMeta.addEnchant(Enchantment.MENDING, 1, true);
        shovelMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
        shovelMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
        shovelMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        diamondShovel.setItemMeta(shovelMeta);

        giveBaseAxe();
        giveBasePickaxe();
        super.player.getInventory().addItem(diamondHoe, diamondShovel, potatoes, carrots, seeds, melon_seeds, pumpkin_seeds, beetroot_seeds, sugarcane, cactus);
    }
}