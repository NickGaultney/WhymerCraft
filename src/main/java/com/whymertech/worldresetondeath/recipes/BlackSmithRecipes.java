package com.whymertech.worldresetondeath.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.NamespacedKey;

public class BlackSmithRecipes {

    private final JavaPlugin plugin;

    public BlackSmithRecipes(JavaPlugin plugin) {
        this.plugin = plugin;
        registerCustomSmithingRecipes();
    }

    private void registerCustomSmithingRecipes() {
        // Example: Iron Chestplate to Diamond Chestplate
        registerCustomSmithingRecipe("iron_to_diamond_chestplate", Material.IRON_CHESTPLATE, Material.DIAMOND, Material.DIAMOND_CHESTPLATE);
        registerCustomSmithingRecipe("iron_to_diamond_helmet", Material.IRON_HELMET, Material.DIAMOND, Material.DIAMOND_HELMET);
        registerCustomSmithingRecipe("iron_to_diamond_boots", Material.IRON_BOOTS, Material.DIAMOND, Material.DIAMOND_BOOTS);
        registerCustomSmithingRecipe("iron_to_diamond_leggings", Material.IRON_LEGGINGS, Material.DIAMOND, Material.DIAMOND_LEGGINGS);

        registerCustomSmithingRecipe("iron_to_diamond_axe", Material.IRON_AXE, Material.DIAMOND, Material.DIAMOND_AXE);
        registerCustomSmithingRecipe("iron_to_diamond_hoe", Material.IRON_HOE, Material.DIAMOND, Material.DIAMOND_HOE);
        registerCustomSmithingRecipe("iron_to_diamond_pickaxe", Material.IRON_PICKAXE, Material.DIAMOND, Material.DIAMOND_PICKAXE);
        registerCustomSmithingRecipe("iron_to_diamond_shovel", Material.IRON_SHOVEL, Material.DIAMOND, Material.DIAMOND_SHOVEL);
        registerCustomSmithingRecipe("iron_to_diamond_sword", Material.IRON_SWORD, Material.DIAMOND, Material.DIAMOND_SWORD);
        // Add additional custom recipes as needed
    }

    private void registerCustomSmithingRecipe(String key, Material base, Material addition, Material result) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, key);
        SmithingTransformRecipe smithingRecipe = new SmithingTransformRecipe(recipeKey, new ItemStack(result), 
            new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            new RecipeChoice.MaterialChoice(base), 
            new RecipeChoice.MaterialChoice(addition));
        Bukkit.addRecipe(smithingRecipe);
    }
}
