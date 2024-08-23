package com.whymertech.worldresetondeath;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;


import com.whymertech.worldresetondeath.roles.AdventurerRole;
import com.whymertech.worldresetondeath.roles.ArcherRole;
import com.whymertech.worldresetondeath.roles.FarmerRole;
import com.whymertech.worldresetondeath.roles.FisherManRole;
import com.whymertech.worldresetondeath.roles.MinerRole;
import com.whymertech.worldresetondeath.roles.RogueRole;
import com.whymertech.worldresetondeath.roles.DrownedRole;
import com.whymertech.worldresetondeath.roles.BlackSmithRole;
import com.whymertech.worldresetondeath.roles.EnchanterRole;
import com.whymertech.worldresetondeath.roles.TheHatRole;
import com.whymertech.worldresetondeath.roles.UndeadRole;
import com.whymertech.worldresetondeath.roles.Role;

import java.io.File;
import java.io.IOException;

public class GameManager {

    public static final String WORLD_NAME = "world";
    public static final String LOBBY_WORLD_NAME = "lobby";
    public static final long RECREATE_DELAY = 100L; // 5 second delay
    //public static final long SEED = -950547527103331411L;
    public static final long SEED = -123456789;

    private File gameLogFile;
    private int gameNumber;
    private SeedManager seedManager;
    private ObjectiveManager objectiveManager;
    public double mobMultiplier = 1.0;
    public Material objectiveMaterial;

    private Plugin plugin;

    public GameManager(Plugin plugin) {
        this.plugin = plugin;
        this.seedManager = new SeedManager(plugin);
        this.objectiveManager = new ObjectiveManager(plugin, this);
        
        initGameLogFile();
        loadWorld();
    }

    public boolean loadWorld() {
        if (worldExists(WORLD_NAME)) {
            WorldCreator loadWorld = new WorldCreator(WORLD_NAME);
            Bukkit.createWorld(loadWorld);

            WorldCreator loadWorldNether = new WorldCreator(WORLD_NAME + "_nether");
            loadWorldNether.environment(World.Environment.NETHER);
            Bukkit.createWorld(loadWorldNether);

            WorldCreator loadWorldEnd = new WorldCreator(WORLD_NAME + "_the_end");
            loadWorldEnd.environment(World.Environment.THE_END);
            Bukkit.createWorld(loadWorldEnd);
            plugin.getLogger().info("Loading your hardcore world");

            loadObjective();
            plugin.getLogger().info("Loading the objective");
            return true;
        }
        plugin.getLogger().info("Coulnd't find world to load");

        return false;
    }

    public boolean worldExists(String worldName) {
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        return worldFolder.exists() && worldFolder.isDirectory();
    }

    public boolean playerHasJoined(Player player) {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        return gameLog.getString("games.game_" + gameNumber + ".players." + player.getName() + ".uuid", "").equals(player.getUniqueId().toString());
    }

    public void addPlayer(Player player) {

        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        // Update the game players in the YAML file
        gameLog.set("games.game_" + gameNumber + ".players." + player.getName() + ".uuid", player.getUniqueId().toString());

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to add player to game log file: " + e.getMessage());
        }

        player.setGameMode(GameMode.SURVIVAL); // Set player game mode to survival
        player.getInventory().clear(); // Clear player inventory
        player.setLevel(30);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20); // Refill health
        player.setFoodLevel(20); // Refill hunger
        player.setSaturation(20); // Refill saturation

        player.setInvulnerable(false);
        
        removeAllPotionEffects(player);

        Role playerRole = getRole(player);
        playerRole.preparePlayer();

        Location roleSpawnLocation = playerRole.getRoleSpawnLocation();
        if (roleSpawnLocation != null) {
            player.teleport(roleSpawnLocation);
        } else {
            player.teleport(Bukkit.getWorld(WORLD_NAME).getSpawnLocation());
        }
    }

    public void resetPlayer(Player player) {
        if (player == null) return;
        
        Role playerRole = getRole(player);
        if (playerRole != null) {
            playerRole.resetPlayer();
        }

        player.setGameMode(GameMode.ADVENTURE); // Set player game mode to survival
        player.getInventory().clear();
        player.setLevel(0);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);

        player.setInvulnerable(true);
        removeAllPotionEffects(player);

        Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();
        while (advancements.hasNext()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancements.next());
            for (String s : progress.getAwardedCriteria())
                progress.revokeCriteria(s);
        }

        player.setStatistic(Statistic.TIME_SINCE_REST, 0);

        player.teleport(getLobbySpawnLocation());
        player.getInventory().addItem(LobbyManager.createLobbyItem());
    }

    public void resetGame() {
        prepareWorldReset();

        new BukkitRunnable() {
            @Override
            public void run() {
                removeWorlds();
                recreateWorlds(); 
            }
        }.runTaskLater(plugin, RECREATE_DELAY); // Delay to give time for death event to be fully processed
        // TODO: Is the delay even neccessary anymore?
        
    }

    private void updateGameData() {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);
        objectiveMaterial = objectiveManager.selectRandomObjective();
        gameNumber++;

        gameLog.set("current_game.number", gameNumber);
        gameLog.set("current_game.objective", objectiveMaterial.getKey().toString());
        gameLog.set("games.game_" + gameNumber + ".number", gameNumber);
        gameLog.set("games.game_" + gameNumber + ".mobMultiplier", 1.0);
        gameLog.set("games.game_" + gameNumber + ".deadPlayers", 0);
        gameLog.set("games.game_" + gameNumber + ".objective", objectiveMaterial.getKey().toString());

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save game log file in \"resetGame()\": " + e.getMessage());
        }
    }

    public void updatePlayerDeaths() {
        mobMultiplier += 0.25;
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        gameLog.set("games.game_" + gameNumber + ".mobMultiplier", mobMultiplier);
        gameLog.set("games.game_" + gameNumber + ".deadPlayers", 1);

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save game log file in \"updatePlayerDeaths()\": " + e.getMessage());
        }
    }

    public void resetPlayerDeathCount() {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        gameLog.set("games.game_" + gameNumber + ".deadPlayers", 0);

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save game log file in \"resetPlayerDeathCount()\": " + e.getMessage());
        }
    }

    private Location getLobbySpawnLocation() {
        return Bukkit.getWorld(LOBBY_WORLD_NAME).getSpawnLocation();
    }

    public void prepareWorldReset() {
        plugin.getLogger().info("Preparing to reset world...");
        mobMultiplier = 1.0;

        World tempWorld = Bukkit.getWorld(LOBBY_WORLD_NAME);
        if (tempWorld == null) {
            WorldCreator creator = new WorldCreator(LOBBY_WORLD_NAME);
            tempWorld = Bukkit.createWorld(creator);
            tempWorld.setDifficulty(Difficulty.PEACEFUL);
        }

        // Teleport all players to the temporary world
        plugin.getLogger().info("Teleporting players to temporary world...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            resetPlayer(player);
        }
    }

    private void removeWorlds() {
        removeWorld();
        removeWorldNether();
        removeWorldEnd();
    }

    private void removeWorld() {
        plugin.getLogger().info("Attempting to remove world...");
        World world = Bukkit.getWorld(WORLD_NAME);
        if (world != null) {
            plugin.getLogger().info("Unloading world: " + WORLD_NAME);
            boolean unload = Bukkit.unloadWorld(world, false);
            plugin.getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            plugin.getLogger().info("Deleting world folder: " + WORLD_NAME);
            deleteWorldFolder(WORLD_NAME);
        } else {
            plugin.getLogger().warning("World " + WORLD_NAME + " not found!");
        }
    }
    
    private void removeWorldNether() {
        plugin.getLogger().info("Attempting to remove the nether...");
        String worldName = WORLD_NAME + "_nether";
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            plugin.getLogger().info("Unloading world: " + worldName);
            boolean unload = Bukkit.unloadWorld(world, false);
            plugin.getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            plugin.getLogger().info("Deleting world folder: " + worldName);
            deleteWorldFolder(worldName);
        } else {
            plugin.getLogger().warning("World " + worldName + " not found!");
        }
    }

    private void removeWorldEnd() {
        plugin.getLogger().info("Attempting to remove the nether...");
        String worldName = WORLD_NAME + "_the_end";
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            plugin.getLogger().info("Unloading world: " + worldName);
            boolean unload = Bukkit.unloadWorld(world, false);
            plugin.getLogger().info("Did Unload Succeed?: " + unload);

            // Delete the world folder
            plugin.getLogger().info("Deleting world folder: " + worldName);
            deleteWorldFolder(worldName);
        } else {
            plugin.getLogger().warning("World " + worldName + " not found!");
        }
    }

    private void deleteWorldFolder(String worldName) {
        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        try {
            FileUtils.deleteDirectory(worldFolder);
            plugin.getLogger().info("World folder deleted successfully: " + worldName);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to delete world folder: " + worldName);
            e.printStackTrace();
        }
    }

    private void recreateWorlds() {
        Bukkit.broadcastMessage("Creating the overworld...");
        recreateWorld();
        Bukkit.broadcastMessage("Creating the nether...");
        recreateWorldNether();
        Bukkit.broadcastMessage("Creating The End...");
        recreateWorldEnd();

        updateGameData();
        objectiveManager.resetGlobalInventory();
        objectiveManager.giveObjectiveBook();
        Bukkit.broadcastMessage("Worlds Loaded.");
    }

    private World recreateWorld() {
        plugin.getLogger().info("Attempting to create world...");
        // Create a new world with the specified seed and set the difficulty to hardcore
        //plugin.getLogger().info("Creating new world with seed: " + GameManager.SEED);
        WorldCreator creator = new WorldCreator(GameManager.WORLD_NAME);
        Long seed = seedManager.getRandomSeedFromList();
        creator.seed(seed);
        World newWorld = Bukkit.createWorld(creator);
        newWorld.setDifficulty(Difficulty.HARD);
        newWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        newWorld.setGameRule(GameRule.KEEP_INVENTORY, true);

        plugin.getLogger().info("World has been reset with seed " + seed + " and set to hardcore mode!");
        objectiveManager.createSpecialEnderChest(newWorld.getSpawnLocation());
        
        return newWorld;
    }
    
    private void recreateWorldNether() {
        plugin.getLogger().info("Attempting to create world nether...");

        WorldCreator loadWorldNether = new WorldCreator(GameManager.WORLD_NAME + "_nether");
        loadWorldNether.environment(World.Environment.NETHER);
        World newWorld = Bukkit.createWorld(loadWorldNether);
        newWorld.setDifficulty(Difficulty.HARD);
        newWorld.setPVP(true);
        newWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        newWorld.setGameRule(GameRule.KEEP_INVENTORY, true);

        plugin.getLogger().info("Nether has been created");
    }

    private void recreateWorldEnd() {
        plugin.getLogger().info("Attempting to create the end...");
        
        WorldCreator loadWorldEnd = new WorldCreator(GameManager.WORLD_NAME + "_the_end");
        loadWorldEnd.environment(World.Environment.THE_END);
        World newWorld = Bukkit.createWorld(loadWorldEnd);
        newWorld.setDifficulty(Difficulty.HARD);
        newWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        newWorld.setGameRule(GameRule.KEEP_INVENTORY, true);

        plugin.getLogger().info("The End has been created");
    }

    public File getGameLogFile() {
        return gameLogFile;
    }

    private void initGameLogFile() {
        gameLogFile = new File(plugin.getDataFolder(), "game_log.yml");
        if (!gameLogFile.exists()) {
            try {
                gameLogFile.getParentFile().mkdirs();
                gameLogFile.createNewFile();
                // Initialize the YAML file with an empty configuration
                YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

                // Get the current game number
                gameNumber = gameLog.getInt("current_game.number", 0);

                gameLog.set("current_game.number", gameNumber);
                gameLog.save(gameLogFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create death log file: " + e.getMessage());
            }
        } else {
            YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);
            gameNumber = gameLog.getInt("current_game.number", 0);
            mobMultiplier = gameLog.getDouble("games.game_" + gameNumber + ".mobMultiplier", 1.0);
            String storedObjective = gameLog.getString("current_game.objective");
            if (storedObjective != null) {
                objectiveMaterial = objectiveManager.getMaterialFromString(storedObjective);
            } else {
                objectiveMaterial = objectiveManager.selectRandomObjective();
                gameLog.set("current_game.objective", objectiveMaterial.getKey().toString());
            }
            objectiveManager.giveObjectiveBook();
        }
    }

    public void setRole(Player player, Role role) {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        // Update the player role in the YAML file
        gameLog.set("games.game_" + gameNumber + ".players." + player.getName() + ".role", role.name());

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to add player role to game log file: " + e.getMessage());
        }
    }

    public void removeRole(Player player) {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        // Update the player role in the YAML file
        gameLog.set("games.game_" + gameNumber + ".players." + player.getName(), "");

        // Save the updated game log
        try {
            gameLog.save(gameLogFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to remove player role from game log file: " + e.getMessage());
        }
    }

    public Role getRole(Player player) {
        YamlConfiguration gameLog = YamlConfiguration.loadConfiguration(gameLogFile);

        String roleName =  gameLog.getString("games.game_" + gameNumber + ".players." + player.getName() + ".role", "");
        return getRoleFromName(player, roleName);
    }

    public void setGameObjective() {
        objectiveManager.selectRandomObjective();
    }

    public Role getRoleFromName(Player player, String roleName) {
        Role role;
        switch (roleName.toLowerCase()) {
            case "adventurer":
                role = new AdventurerRole(player);
                break;
            case "miner":
                role = new MinerRole(player);
                break;
            case "farmer":
                role = new FarmerRole(player);
                break;
            case "rogue":
                role = new RogueRole(player);
                break;
            case "archer":
                role = new ArcherRole(player);
                break;
            case "fisherman":
                role = new FisherManRole(player);
                break;
            case "drowned":
                role = new DrownedRole(player);
                break;
            case "blacksmith":
                role = new BlackSmithRole(player);
                break;
            case "enchanter":
                role = new EnchanterRole(player);
                break;
            case "thehat":
                role = new TheHatRole(player);
                break;
            case "undead":
                role = new UndeadRole(player);
                break;
            default:
                role = null;
                break;
        }

        return role;
    }

    public void removeAllPotionEffects(Player player) {
        if (player != null) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
    }

    public SeedManager getSeedManager() {
        return seedManager;
    }

    public ObjectiveManager getObjectiveManager() {
        return objectiveManager;
    }

    public Material getObjectiveMaterial() {
        return objectiveMaterial;
    }

    private void loadObjective() {
        objectiveManager.giveObjectiveBook();
    }
}
