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

public class FisherManRole extends GenericRole {

    public double health = 20.0;
    public int speedLevel = 1;

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
}
