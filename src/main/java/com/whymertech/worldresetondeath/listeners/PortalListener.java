package com.whymertech.worldresetondeath.listeners;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World fromWorld = player.getWorld();

        String worldName = GameManager.WORLD_NAME;
        String netherWorldName = GameManager.WORLD_NAME + "_nether";
        //String endWorldName = GameManager.WORLD_NAME + "_the_end";


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
        } else {
            plugin.getLogger().warning("could not teleport");
        }
    }
}
