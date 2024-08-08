package com.whymertech.worldresetondeath.roles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class RogueRole extends GenericRole {
    public double health = 14.0;
    public int speedLevel = 4;

    public RogueRole(Player player) {
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
        return "Rogue";
    }

    public void giveItems() {
        ItemStack rogueSword = new ItemStack(Material.WOODEN_SWORD);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        ItemMeta rogueSwordMeta = rogueSword.getItemMeta();
        if (rogueSwordMeta != null) {
            rogueSwordMeta.addEnchant(Enchantment.MENDING, 1, true);
            rogueSwordMeta.addEnchant(Enchantment.UNBREAKING, 255, true);

            // Use PersistentDataContainer to store custom attributes
            NamespacedKey key = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    key, 
                    40.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            rogueSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedAttributeModifier = new AttributeModifier(
                    key,
                    -3.6,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            rogueSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedAttributeModifier);

            rogueSword.setItemMeta(rogueSwordMeta);
        }

        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 8, true);
        bootsMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
        boots.setItemMeta(bootsMeta);

        giveBaseAxe();
        giveBasePickaxe();
        giveBaseShovel();
        super.player.getInventory().addItem(rogueSword, helmet, chestPlate, leggings, boots);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }
}
