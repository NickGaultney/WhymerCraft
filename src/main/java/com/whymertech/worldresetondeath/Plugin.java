package com.whymertech.worldresetondeath;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import org.bukkit.configuration.file.YamlConfiguration;


/*
 * worldresetondeath java plugin
 */
public class Plugin extends JavaPlugin implements Listener
{
    public static final String WORLD_NAME = "world";
    public static final String TEMP_WORLD_NAME = "lobby";
    public static final long RECREATE_DELAY = 100L; // 1 second delay
    public static final long SEED = -950547527103331411L;
    private File deathLogFile;

    @Override
    public void onEnable() {
        initDeathLogFile();

        getServer().getPluginManager().registerEvents(this, this); // Registering the main class as the listener
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);


        getLogger().info("WorldResetOnDeath plugin has been enabled!");
        getCommand("kys").setExecutor(new KysCommand(this)); // Registering the kys command
        getCommand("join").setExecutor(new JoinCommand(this)); // Registering the join command
        getCommand("lobby").setExecutor(new LobbyCommand(this)); // Registering the lobby command
        loadWorld(WORLD_NAME);
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldResetOnDeath plugin has been disabled!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                prepareWorldReset();
            }
        }.runTaskLater(this, RECREATE_DELAY); // Delay to give time for death event to be fully processed
    }

    public void teleportPlayerToWorld(Player player) {
        World world = Bukkit.getWorld(WORLD_NAME);
        player.teleport(world.getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL); // Set player game mode to survival
        //player.getInventory().clear(); // Clear player inventor
        player.setHealth(20); // Refill health
        player.setFoodLevel(20); // Refill hunger
        player.setSaturation(20); // Refill saturation
    }

    public void teleportPlayerToLobby(Player player) {
        World world = Bukkit.getWorld(TEMP_WORLD_NAME);
        player.teleport(world.getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE); // Set player game mode to survival
        //player.getInventory().clear(); // Clear player inventor
        //player.setHealth(20); // Refill health
        //player.setFoodLevel(20); // Refill hunger
        //player.setSaturation(20); // Refill saturation
    }

    private void initDeathLogFile() {
        deathLogFile = new File(getDataFolder(), "death_log.yml");
        if (!deathLogFile.exists()) {
            try {
                deathLogFile.getParentFile().mkdirs();
                deathLogFile.createNewFile();
                // Initialize the YAML file with an empty configuration
                YamlConfiguration deathLog = YamlConfiguration.loadConfiguration(deathLogFile);
                deathLog.save(deathLogFile);
            } catch (IOException e) {
                getLogger().severe("Failed to create death log file: " + e.getMessage());
            }
        }
    }

    public File getDeathLogFile() {
        return deathLogFile;
    }

    private boolean loadWorld(String worldName) {
        if (worldExists(WORLD_NAME)) {
            WorldCreator loadWorld = new WorldCreator(WORLD_NAME);
            Bukkit.createWorld(loadWorld);

            WorldCreator loadWorldNether = new WorldCreator(WORLD_NAME + "_nether");
            loadWorldNether.environment(World.Environment.NETHER);
            Bukkit.createWorld(loadWorldNether);

            WorldCreator loadWorldEnd = new WorldCreator(WORLD_NAME + "_the_end");
            loadWorldEnd.environment(World.Environment.THE_END);
            Bukkit.createWorld(loadWorldEnd);
            getLogger().info("Loading your hardcore world");
            return true;
        }
        getLogger().info("Coulnd't find world to load");

        return false;
    }

    public boolean worldExists(String worldName) {
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        return worldFolder.exists() && worldFolder.isDirectory();
    }
        

    private void prepareWorldReset() {
        getLogger().info("Preparing to reset world...");

        World tempWorld = Bukkit.getWorld(TEMP_WORLD_NAME);
        if (tempWorld == null) {
            WorldCreator creator = new WorldCreator(TEMP_WORLD_NAME);
            tempWorld = Bukkit.createWorld(creator);
            tempWorld.setDifficulty(Difficulty.PEACEFUL);
        }

        // Teleport all players to the temporary world
        getLogger().info("Teleporting players to temporary world...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(tempWorld.getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE); // Set player game mode to survival
            player.getInventory().clear(); // Clear player inventor
            player.setHealth(20); // Refill health
            player.setFoodLevel(20); // Refill hunger
            player.setSaturation(20); // Refill saturation
        }

        removeWorlds();
    }

    private void removeWorlds() {
        removeWorld();
        removeWorldNether();
        removeWorldEnd();
    }

    private void removeWorld() {
        getLogger().info("Attempting to remove world...");
        World world = Bukkit.getWorld(WORLD_NAME);
        if (world != null) {
            getLogger().info("Unloading world: " + WORLD_NAME);
            boolean unload = Bukkit.unloadWorld(world, false);
            getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            getLogger().info("Deleting world folder: " + WORLD_NAME);
            deleteWorldFolder(WORLD_NAME);
        } else {
            getLogger().warning("World " + WORLD_NAME + " not found!");
        }
    }
    
    private void removeWorldNether() {
        getLogger().info("Attempting to remove the nether...");
        String worldName = WORLD_NAME + "_nether";
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            getLogger().info("Unloading world: " + worldName);
            boolean unload = Bukkit.unloadWorld(world, false);
            getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            getLogger().info("Deleting world folder: " + worldName);
            deleteWorldFolder(worldName);
        } else {
            getLogger().warning("World " + worldName + " not found!");
        }
    }

    private void removeWorldEnd() {
        getLogger().info("Attempting to remove the nether...");
        String worldName = WORLD_NAME + "_the_end";
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            getLogger().info("Unloading world: " + worldName);
            boolean unload = Bukkit.unloadWorld(world, false);
            getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            getLogger().info("Deleting world folder: " + worldName);
            deleteWorldFolder(worldName);
        } else {
            getLogger().warning("World " + worldName + " not found!");
        }
    }

    private void deleteWorldFolder(String worldName) {
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        try {
            FileUtils.deleteDirectory(worldFolder);
            getLogger().info("World folder deleted successfully: " + worldName);
        } catch (IOException e) {
            getLogger().severe("Failed to delete world folder: " + worldName);
            e.printStackTrace();
        }
    }
}