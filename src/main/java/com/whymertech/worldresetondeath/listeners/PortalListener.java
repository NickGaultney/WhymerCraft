package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.whymertech.worldresetondeath.GameManager;
import com.whymertech.worldresetondeath.Plugin;

public class PortalListener implements Listener {

    private final Plugin plugin;

    public PortalListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        plugin.getLogger().info("player portal event");
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World fromWorld = player.getWorld();

        String worldName = GameManager.WORLD_NAME;
        String netherWorldName = GameManager.WORLD_NAME + "_nether";
        String endWorldName = GameManager.WORLD_NAME + "_the_end";


        if (event.getCause() == PlayerPortalEvent.TeleportCause.NETHER_PORTAL) {
            if (fromWorld.getName().equals(worldName)) {
                // Teleport player to the correct Nether world
                World netherWorld = Bukkit.getWorld(netherWorldName);
                if (netherWorld != null) {
                    Location netherLocation = new Location(netherWorld, location.getX() / 8, location.getY(), location.getZ() / 8);
                    event.setTo(netherLocation);
                    player.sendMessage("Teleporting to " + netherWorld.getName());
                    plugin.getLogger().info("Teleporting to " + netherWorld.getName());
                }
            } else if (fromWorld.getName().equals(netherWorldName)) {
                // Teleport player back to the main world
                World mainWorld = Bukkit.getWorld(worldName);
                if (mainWorld != null) {
                    Location mainWorldLocation = new Location(mainWorld, location.getX() * 8, location.getY(), location.getZ() * 8);
                    event.setTo(mainWorldLocation);
                    player.sendMessage("Teleporting to " + mainWorld.getName());
                    plugin.getLogger().info("Teleporting to " + mainWorld.getName());
                }
            }
        } else if (event.getCause() == PlayerPortalEvent.TeleportCause.END_PORTAL) {
            if (fromWorld.getName().equals(worldName)) {
                World endWorld = Bukkit.getWorld(endWorldName);
                if (endWorld != null) {
                    // Define a custom spawn location in the void
                    Location endLocation = new Location(endWorld, 100, 48, 0); // Example coordinates off in the void
        
                    // Create a 5x5 obsidian platform at the spawn location
                    createObsidianPlatform(endLocation);
        
                    // Teleport the player to the platform
                    event.setTo(endLocation.add(0, 1, 0)); // Move the player one block above the platform
                    player.sendMessage("Teleporting to " + endWorld.getName());
                    plugin.getLogger().info("Teleporting to " + endWorld.getName());
                }
            }        
        } else {
            plugin.getLogger().warning("could not teleport");
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        // String worldName = GameManager.WORLD_NAME;
        // String netherWorldName = GameManager.WORLD_NAME + "_nether";
        // String endWorldName = GameManager.WORLD_NAME + "_the_end";

        Location from = event.getFrom();
        Location to = event.getTo();

        String[] startComponents = from.getWorld().getName().split("_", 2);
        String[] endComponents = to.getWorld().getName().split("_", 2);


        String newTo = to.getWorld().getName().replace(endComponents[0], startComponents[0]);

        to.setWorld(Bukkit.getWorld(newTo));

    }

    // Method to create a 5x5 obsidian platform and clear space above
    private void createObsidianPlatform(Location center) {
        World world = center.getWorld();
        int startX = center.getBlockX() - 2;
        int startY = center.getBlockY();
        int startZ = center.getBlockZ() - 2;

        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                // Set the platform block to obsidian
                Location blockLocation = new Location(world, startX + x, startY, startZ + z);
                world.getBlockAt(blockLocation).setType(Material.OBSIDIAN);

                // Clear the 3 blocks above the platform
                for (int y = 1; y <= 3; y++) {
                    Location aboveBlockLocation = blockLocation.clone().add(0, y, 0);
                    world.getBlockAt(aboveBlockLocation).setType(Material.AIR);
                }
            }
        }
    }
}
