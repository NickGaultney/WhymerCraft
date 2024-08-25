package com.whymertech.worldresetondeath.roles;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FarmerRole extends GenericRole implements Listener {

    public FarmerRole(Player player) {
        super(player);
    }

    public FarmerRole(GameManager gameManager) {
        super(gameManager);
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
        ItemStack pickle = new ItemStack(Material.SEA_PICKLE, 1);
        ItemStack bamboo = new ItemStack(Material.BAMBOO, 1);
        ItemStack coco = new ItemStack(Material.COCOA_BEANS, 1);
        ItemStack warts = new ItemStack(Material.NETHER_WART, 1);
        ItemStack berry = new ItemStack(Material.SWEET_BERRY_BUSH, 1);
        ItemStack kelp = new ItemStack(Material.KELP, 1);
        ItemStack glow = new ItemStack(Material.GLOW_BERRIES, 1);
        ItemStack redMush = new ItemStack(Material.RED_MUSHROOM, 1);
        ItemStack brownMush = new ItemStack(Material.BROWN_MUSHROOM, 1);
        ItemStack chorusFruit = new ItemStack(Material.CHORUS_FRUIT, 1);
        ItemStack sapling = new ItemStack(Material.OAK_SAPLING, 1);

        ItemStack endStone = new ItemStack(Material.END_STONE, 32);
        ItemStack soulSand = new ItemStack(Material.SOUL_SAND, 32);
        ItemStack grass = new ItemStack(Material.GRASS_BLOCK, 32);
        ItemStack boneMeal = new ItemStack(Material.BONE_MEAL, 16);
        ItemStack water = new ItemStack(Material.WATER_BUCKET, 2);

        
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
        super.player.getInventory().addItem(pickle, bamboo, coco, warts, berry, kelp, glow, redMush, brownMush, endStone, chorusFruit, water, sapling, soulSand, grass, boneMeal);
    }

    @Override
    public Material favoriteFood() {
        return Material.CARROT;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Role playerRole = gameManager.getRole(player);

        // Check if the entity being clicked is a player
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }

        Player targetPlayer = (Player) event.getRightClicked();
        if (targetPlayer == null) return;

        Role targetRole = gameManager.getRole(targetPlayer);

        // Check if the player has the Farmer role
        if (playerRole instanceof FarmerRole) {
            // Check if the player is holding bread
            ItemStack food = player.getInventory().getItemInMainHand();
            if (food.getType() == targetRole.favoriteFood()) {
                int breadCount = food.getAmount();

                // Increase the duration of positive potion effects for both players
                increasePotionDuration(player, breadCount);
                increasePotionDuration(targetPlayer, breadCount);
    
                // Consume the bread stack
                food.setAmount(0);
                player.getInventory().setItemInMainHand(null);
            } else {
                player.sendMessage("The " + targetRole.name() + "\'s favorite food is " + targetRole.favoriteFood().toString());
            }
        }
    }

    private void increasePotionDuration(Player player, int minutes) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            // Check if the potion effect is positive
            if (isPositiveEffect(effect)) {
                int newDuration = effect.getDuration() + (minutes * 60 * 20); // 1 minute = 60 seconds * 20 ticks
                PotionEffect newEffect = new PotionEffect(effect.getType(), newDuration, effect.getAmplifier(), effect.isAmbient(), false, effect.hasIcon());
                player.addPotionEffect(newEffect);
            }
        }
    }

    private boolean isPositiveEffect(PotionEffect effect) {
        NamespacedKey key = effect.getType().getKey();
        switch (key.getKey()) {
            case "speed":
            case "fast_digging": // Haste
            case "increase_damage": // Strength
            case "jump":
            case "damage_resistance":
            case "fire_resistance":
            case "water_breathing":
            case "invisibility":
            case "night_vision":
            case "luck":
            case "conduit_power":
            case "dolphins_grace":
            case "hero_of_the_village":
                return true;
            default:
                return false;
        }
    }
}