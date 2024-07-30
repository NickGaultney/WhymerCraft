package com.whymertech.worldresetondeath;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

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

        if (event.getCause() == PlayerPortalEvent.TeleportCause.NETHER_PORTAL) {
            if (fromWorld.getName().equals("world")) {
                // Teleport player to the correct Nether world
                World netherWorld = Bukkit.getWorld("world_nether");
                if (netherWorld != null) {
                    Location netherLocation = new Location(netherWorld, location.getX() / 8, location.getY(), location.getZ() / 8);
                    event.setTo(netherLocation);
                    player.sendMessage("Teleporting to " + netherWorld.getName());
                    plugin.getLogger().info("Teleporting to " + netherWorld.getName());
                }
            } else if (fromWorld.getName().equals("world_nether")) {
                // Teleport player back to the main world
                World mainWorld = Bukkit.getWorld("world");
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
