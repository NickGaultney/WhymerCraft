package com.whymertech.worldresetondeath.roles;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.whymertech.worldresetondeath.GameManager;

public class ArcherRole extends GenericRole{
    public double health = 16.0;
    public int speedLevel = 4;

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
        super.addSpeedEffect(speedLevel);
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

        ItemMeta bowMeta = bow.getItemMeta();
        if (bowMeta != null) {
            bowMeta.addEnchant(Enchantment.INFINITY, 1, true);
            bowMeta.addEnchant(Enchantment.PUNCH, 2, true);
            bowMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
            bowMeta.addEnchant(Enchantment.MENDING, 1, true);

            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    UUID.randomUUID(), 
                    "generic.attackDamage", 
                    6.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            );
            bowMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
            
            bow.setItemMeta(bowMeta);
        }

        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.addEnchant(Enchantment.FEATHER_FALLING, 5, true);
        bootsMeta.addEnchant(Enchantment.UNBREAKING, 255, true);
        boots.setItemMeta(bootsMeta);

        giveBaseAxe();
        giveBasePickaxe();
        giveBaseShovel();
        super.player.getInventory().addItem(bow, helmet, chestPlate, leggings, boots, arrow);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }
}
