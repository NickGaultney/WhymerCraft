package com.whymertech.worldresetondeath.listeners;

import org.bukkit.event.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

public class LobbyItemListener implements Listener {

    GameManager gameManager;
    Plugin plugin;
    public LobbyItemListener() {

    }

    public LobbyItemListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Select Your Role")) {
            handleRoleSelection(event);
        } else if (event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Role Info")) {
            handleRoleInfo(event);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.ENDER_EYE) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                NamespacedKey key = new NamespacedKey("worldresetondeath", "unique_eye");

                // Check if the item has the unique key
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    // Open the menu if the Eye of Ender is the unique one
                    openRoleSelectorMenu(player);
                    event.setCancelled(true);
                }
            }
        }
    }

    public void openRoleSelectorMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Select Your Role");

        // Add different colored wool to the menu
        menu.setItem(10, createMenuItem(Material.WOODEN_SWORD, ChatColor.RED + "Rogue"));
        menu.setItem(12, createMenuItem(Material.DIAMOND_PICKAXE, ChatColor.GREEN + "Miner"));
        menu.setItem(14, createMenuItem(Material.IRON_CHESTPLATE, ChatColor.BLUE + "Adventurer"));
        menu.setItem(16, createMenuItem(Material.DIAMOND_HOE, ChatColor.GOLD + "Farmer"));
        menu.setItem(19, createMenuItem(Material.BOW, ChatColor.RED + "Archer"));
        menu.setItem(21, createMenuItem(Material.TRIDENT, ChatColor.GREEN + "Drowned"));
        menu.setItem(23, createMenuItem(Material.ANVIL, ChatColor.BLUE + "Blacksmith"));
        menu.setItem(25, createMenuItem(Material.ENCHANTING_TABLE, ChatColor.GOLD + "Enchanter"));
        menu.setItem(28, createMenuItem(Material.ELYTRA, ChatColor.RED + "The Hat"));
        menu.setItem(30, createMenuItem(Material.ZOMBIE_HEAD, ChatColor.GREEN + "Undead"));
        menu.setItem(32, createMenuItem(Material.FISHING_ROD, ChatColor.BLUE + "Fisherman"));

        menu.setItem(45, createMenuItem(Material.LECTERN, ChatColor.WHITE + "Role Info"));

        // Open the menu
        player.openInventory(menu);
    }

    public void openRoleInfoMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Role Info");

        // Add different colored wool to the menu
        menu.setItem(10, createMenuItem(Material.WOODEN_SWORD, ChatColor.RED + "Rogue"));
        menu.setItem(12, createMenuItem(Material.DIAMOND_PICKAXE, ChatColor.GREEN + "Miner"));
        menu.setItem(14, createMenuItem(Material.IRON_CHESTPLATE, ChatColor.BLUE + "Adventurer"));
        menu.setItem(16, createMenuItem(Material.DIAMOND_HOE, ChatColor.GOLD + "Farmer"));
        menu.setItem(19, createMenuItem(Material.BOW, ChatColor.RED + "Archer"));
        menu.setItem(21, createMenuItem(Material.TRIDENT, ChatColor.GREEN + "Drowned"));
        menu.setItem(23, createMenuItem(Material.ANVIL, ChatColor.BLUE + "Blacksmith"));
        menu.setItem(25, createMenuItem(Material.ENCHANTING_TABLE, ChatColor.GOLD + "Enchanter"));
        menu.setItem(28, createMenuItem(Material.ELYTRA, ChatColor.RED + "The Hat"));
        menu.setItem(30, createMenuItem(Material.ZOMBIE_HEAD, ChatColor.GREEN + "Undead"));
        menu.setItem(32, createMenuItem(Material.FISHING_ROD, ChatColor.BLUE + "Fisherman"));

        menu.setItem(45, createMenuItem(Material.ENDER_PEARL, ChatColor.WHITE + "Role Selector"));

        // Open the menu
        player.openInventory(menu);
    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void handleRoleSelection(InventoryClickEvent event) {
        event.setCancelled(true); // Prevent taking the item

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Handle the click on different wool types
        switch (clickedItem.getType()) {
            case WOODEN_SWORD:
                player.performCommand("role rogue");
                player.performCommand("join");
                break;
            case DIAMOND_PICKAXE:
                player.performCommand("role miner");
                player.performCommand("join");
                break;
            case IRON_CHESTPLATE:
                player.performCommand("role adventurer");
                player.performCommand("join");
                break;
            case DIAMOND_HOE:
                player.performCommand("role farmer");
                player.performCommand("join");
                break;
            case BOW:
                player.performCommand("role archer");
                player.performCommand("join");
                break;
            case TRIDENT:
                player.performCommand("role drowned");
                player.performCommand("join");
                break;
            case ANVIL:
                player.performCommand("role blacksmith");
                player.performCommand("join");
                break;
            case ENCHANTING_TABLE:
                player.performCommand("role enchanter");
                player.performCommand("join");
                break;
            case PUMPKIN:
                player.performCommand("role thehat");
                player.performCommand("join");
                break;
            case ZOMBIE_HEAD:
                player.performCommand("role undead");
                player.performCommand("join");
                break;
            case FISHING_ROD:
                player.performCommand("role fisherman");
                player.performCommand("join");
                break;
            case LECTERN:
                openRoleInfoMenu(player);
                return;
            default:
                break;
        }
        player.closeInventory(); // Close the menu after selection
    }

    private void handleRoleInfo(InventoryClickEvent event) {
        event.setCancelled(true); // Prevent taking the item

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Handle the click on different wool types
        switch (clickedItem.getType()) {
            case WOODEN_SWORD:
                player.performCommand("role rogue");
                player.performCommand("join");
                break;
            case DIAMOND_PICKAXE:
                player.performCommand("role miner");
                player.performCommand("join");
                break;
            case IRON_CHESTPLATE:
                player.performCommand("role adventurer");
                player.performCommand("join");
                break;
            case DIAMOND_HOE:
                player.performCommand("role farmer");
                player.performCommand("join");
                break;
            case BOW:
                player.performCommand("role archer");
                player.performCommand("join");
                break;
            case TRIDENT:
                player.performCommand("role drowned");
                player.performCommand("join");
                break;
            case ANVIL:
                player.performCommand("role blacksmith");
                player.performCommand("join");
                break;
            case ENCHANTING_TABLE:
                player.performCommand("role enchanter");
                player.performCommand("join");
                break;
            case PUMPKIN:
                player.performCommand("role thehat");
                player.performCommand("join");
                break;
            case ZOMBIE_HEAD:
                player.performCommand("role undead");
                player.performCommand("join");
                break;
            case FISHING_ROD:
                player.performCommand("role fisherman");
                player.performCommand("join");
                break;
            case ENDER_PEARL:
                openRoleSelectorMenu(player);
                return;
            default:
                break;
        }
        player.closeInventory(); // Close the menu after selection
    }
}
