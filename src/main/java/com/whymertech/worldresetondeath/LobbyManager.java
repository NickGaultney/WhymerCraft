package com.whymertech.worldresetondeath;

import org.bukkit.persistence.PersistentDataType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyManager {
    
    // Method to create the unique Eye of Ender
    public static ItemStack createLobbyItem() {
        ItemStack eyeOfEnder = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = eyeOfEnder.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Role Selector");
            // Use PersistentDataContainer to store a unique key
            NamespacedKey key = new NamespacedKey("worldresetondeath", "unique_eye");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "world_selector"); 
            eyeOfEnder.setItemMeta(meta);
        }

        return eyeOfEnder;
    }
}
