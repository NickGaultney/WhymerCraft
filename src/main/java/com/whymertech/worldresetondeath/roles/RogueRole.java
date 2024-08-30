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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.whymertech.worldresetondeath.GameManager;

public class RogueRole extends GenericRole implements Listener {
    public double health = 14.0;
    public int speedLevel = 2;

    public RogueRole(GameManager gameManager) {
        super(gameManager);
    }

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
        addEffects();
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

        super.enchantItem(Enchantment.LOOTING, 5, rogueSword);
        super.enchantItem(Enchantment.MENDING, 1, rogueSword);
        super.enchantItem(Enchantment.UNBREAKING, 255, rogueSword);

        ItemMeta rogueSwordMeta = rogueSword.getItemMeta();
        if (rogueSwordMeta != null) {
            // Use PersistentDataContainer to store custom attributes
            NamespacedKey attackDamageKey = new NamespacedKey("worldresetondeath", "custom_attack_damage");
            NamespacedKey attackSpeedKey = new NamespacedKey("worldresetondeath", "custom_attack_speed");
            // Add custom attributes
            AttributeModifier damageModifier = new AttributeModifier(
                    attackDamageKey, 
                    40.0, // Additional attack damage
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            rogueSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

            AttributeModifier attackSpeedAttributeModifier = new AttributeModifier(
                    attackSpeedKey,
                    -3.4,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            rogueSwordMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedAttributeModifier);

            rogueSword.setItemMeta(rogueSwordMeta);
        }

        super.enchantItem(Enchantment.SWIFT_SNEAK, 5, leggings);
        super.enchantItem(Enchantment.UNBREAKING, 255, leggings);
        super.enchantItem(Enchantment.MENDING, 1, leggings);

        super.enchantItem(Enchantment.FEATHER_FALLING, 8, boots);
        super.enchantItem(Enchantment.UNBREAKING, 255, boots);
        super.enchantItem(Enchantment.MENDING, 1, boots);
        

        giveBaseAxe();
        giveBasePickaxe();
        giveBaseShovel();
        super.player.getInventory().addItem(rogueSword, helmet, chestPlate, leggings, boots);
    }

    @Override
    public boolean canDoubleJump() {
        return true;
    }

    public void addEffects() {
        super.addSpeedEffect(speedLevel);
    }

    @Override
    public Material favoriteFood() {
        return Material.SUGAR;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            // Check if the player has the "rogue" role
            Role playerRole = gameManager.getRole(player);
            if (playerRole != null && playerRole instanceof RogueRole) {
                // Check if the item is a sword
                double newDmg = event.getDamage() * gameManager.mobMultiplier;
                player.sendMessage("Damage: " + newDmg);
                event.setDamage(event.getDamage() * gameManager.mobMultiplier);
            }
        }
    }
}
