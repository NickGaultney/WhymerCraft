package com.whymertech.worldresetondeath;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.*;


import org.bukkit.configuration.file.YamlConfiguration;

public class SeedManager {
    
    private File seedFile;
    private Plugin plugin;
    public String currentSeed;

    public SeedManager(Plugin plugin) {
        this.plugin = plugin;

        initSeedFile();
    }

    public void initSeedFile() {
        seedFile = new File(plugin.getDataFolder(), "seedFile.yml");
        if (!seedFile.exists()) {
            try {
                seedFile.getParentFile().mkdirs();
                seedFile.createNewFile();
                // Initialize the YAML file with an empty configuration
                YamlConfiguration seeds = YamlConfiguration.loadConfiguration(seedFile);

                // Get the current game number
                currentSeed = seeds.getString("current_game.seed", "");

                seeds.set("current_game.number", seedFile);
                seeds.save(seedFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create the seed file: " + e.getMessage());
            }
        } else {
            YamlConfiguration seeds = YamlConfiguration.loadConfiguration(seedFile);
            currentSeed = seeds.getString("current_game.seed", "");
        }
    }

    public File getSeedFile() {
        return seedFile;
    }

    public String getCurrentSeed() {
        return currentSeed;
    }

    public Long getRandomSeedFromList() {
        YamlConfiguration seedConfig = YamlConfiguration.loadConfiguration(seedFile);

        Set<String> seeds = seedConfig.getConfigurationSection("seeds").getKeys(false);
        List<String> seedList = new ArrayList<>(seeds);

        if (seedList.isEmpty()) {
            return getRandomSeed(); // Or handle the case where no seeds are available
        }

        Random random = new Random();
        Collections.shuffle(seedList);
        
        String randomSeed = seedList.get(random.nextInt(seedList.size()));
        
        return Long.parseLong(randomSeed);
    }

    public long getRandomSeed() {
        Random random = new Random();
        return random.nextLong();
    }

    public void addSeed(String name, String seed) {
        YamlConfiguration seeds = YamlConfiguration.loadConfiguration(seedFile);

        // Update the player role in the YAML file
        seeds.set("seeds." + seed + ".name", name);

        // Save the updated game log
        try {
            seeds.save(seedFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to add seed to seed file: " + e.getMessage());
        }
    }
}
