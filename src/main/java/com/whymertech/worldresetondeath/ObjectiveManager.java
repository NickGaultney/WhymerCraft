package com.whymertech.worldresetondeath;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class ObjectiveManager implements Listener {

    private Plugin plugin;
    private Inventory globalInventory;
    private GameManager gameManager;

    public ObjectiveManager(Plugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.globalInventory = Bukkit.createInventory(null, 27, "Objective Chest");
    }

    public void resetGlobalInventory() {
        this.globalInventory = Bukkit.createInventory(null, 27, "Objective Chest");
    }

    public void createSpecialEnderChest(Location location) {
        // Set the block at the location to an Ender Chest
        Block block = location.getBlock();
        block.setType(Material.ENDER_CHEST);
    }

    @EventHandler
    public void onEnderChestOpen(PlayerInteractEvent  event) {
        // Check if the action is a right-click on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            // Check if the block is an Ender Chest
            if (block != null && block.getType() == Material.ENDER_CHEST) {
                Player player = event.getPlayer();
                // Your custom logic here
                //player.sendMessage("You opened an Ender Chest!");

                // For example, cancel the default Ender Chest inventory
                event.setCancelled(true);
                player.openInventory(globalInventory);

                // Open your custom inventory here if needed
                // player.openInventory(customInventory);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == globalInventory) {
            // Get the item being clicked or placed in the chest
            // ItemStack currentItem = event.getCurrentItem();
            // ItemStack cursorItem = event.getCursor();
            Material objectiveItem = gameManager.getObjectiveMaterial();

            // Check for the win condition
            if (globalInventory.contains(objectiveItem)) {
                Bukkit.broadcastMessage("Victory is yours!");
                gameManager.resetGame();
            }

            // // If the player is clicking on an item in the chest (currentItem) to move it
            // if (currentItem != null && currentItem.getType() != objectiveItem) {
            //     event.getWhoClicked().sendMessage("That is a " + currentItem + " not a " + objectiveItem);
            //     event.setCancelled(true); // Prevent moving non-objective items
            // }
        }
    }

    public Material selectRandomObjective() {
        List<Material> objectives = Arrays.asList(
            Material.RECOVERY_COMPASS,
            Material.ELYTRA,
            Material.DRAGON_BREATH,
            Material.NETHER_STAR,
            Material.TOTEM_OF_UNDYING,
            Material.DRAGON_EGG,
            Material.HEAVY_CORE
        );

        Random random = new Random();
        return objectives.get(random.nextInt(objectives.size()));
    }

    public Material getMaterialFromString(String s) {
        switch (s) {
            case "minecraft:recovery_compass":
                return Material.RECOVERY_COMPASS;
            case "minecraft:elytra":
                return Material.ELYTRA;
            case "minecraft:dragon_breath":
                return Material.DRAGON_BREATH;
            case "minecraft:nether_star":
                return Material.NETHER_STAR;
            case "minecraft:totem_of_undying":
                return Material.TOTEM_OF_UNDYING;
            case "minecraft:dragon_egg":
                return Material.DRAGON_EGG;
            case "minecraft:heavy_core":
                return Material.HEAVY_CORE;
            default:
                return null;
        }
    }

    public void giveObjectiveBook() {
        // Get the objective material from the game manager
        Material objectiveMaterial = gameManager.getObjectiveMaterial();

        // Create a new ItemStack for the book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        // Create and set up the book's metadata
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        if (bookMeta != null) {
            bookMeta.setTitle("Objective Material");
            bookMeta.setAuthor("Game Manager");
            
            // Add the material's name as the content of the first page
            bookMeta.addPage("The objective material is: " + objectiveMaterial.name());
            
            // Apply the meta to the book item
            book.setItemMeta(bookMeta);
        }

        // Fill the inventory with the book (placing it in the first slot as an example)
        this.globalInventory.setItem(0, book);
    }
}
