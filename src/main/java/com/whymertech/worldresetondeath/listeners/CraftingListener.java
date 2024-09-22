package com.whymertech.worldresetondeath.listeners;

import com.whymertech.worldresetondeath.Plugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static com.whymertech.worldresetondeath.Utils.plugin;

public class CraftingListener implements Listener {
    private final Plugin plugin;

    public CraftingListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftCheck(PrepareItemCraftEvent event) {
        CraftingInventory crafter =  event.getInventory();
        for (ItemStack items : crafter.getMatrix()) {
            NamespacedKey uncraftable = new NamespacedKey(plugin, "uncraftable");
            if (items == null || !items.hasItemMeta()) {
                continue;
            }
            if (Boolean.TRUE.equals(items.getItemMeta().getPersistentDataContainer().get(uncraftable, PersistentDataType.BOOLEAN))) {
                crafter.setResult(new ItemStack(Material.AIR));
            }
        }
    }

    public static ItemStack setUncraftable(Plugin plugin, ItemStack items) {
        ItemMeta pearlMeta = items.getItemMeta();
        NamespacedKey pearlKey = new NamespacedKey(plugin, "uncraftable");
        pearlMeta.getPersistentDataContainer().set(pearlKey, PersistentDataType.BOOLEAN, true);
        items.setItemMeta(pearlMeta);
        return items;
    }
}
