package com.whymertech.worldresetondeath;

import org.bukkit.plugin.java.JavaPlugin;

import com.whymertech.worldresetondeath.commands.JoinCommand;
import com.whymertech.worldresetondeath.commands.KysCommand;
import com.whymertech.worldresetondeath.commands.LobbyCommand;
import com.whymertech.worldresetondeath.commands.ResetPlayerCommand;
import com.whymertech.worldresetondeath.commands.RoleCommand;
import com.whymertech.worldresetondeath.commands.AddSeedCommand;
import com.whymertech.worldresetondeath.commands.GiveLobbyItemCommand;
import com.whymertech.worldresetondeath.listeners.AnvilListener;
import com.whymertech.worldresetondeath.listeners.DeathListener;
import com.whymertech.worldresetondeath.listeners.PortalListener;
import com.whymertech.worldresetondeath.listeners.PlayerListener;
import com.whymertech.worldresetondeath.listeners.DoubleJumpListener;
import com.whymertech.worldresetondeath.listeners.EnchantmentListener;
import com.whymertech.worldresetondeath.listeners.LobbyItemListener;
import com.whymertech.worldresetondeath.listeners.MobDamageListener;
import com.whymertech.worldresetondeath.tabCompleters.ResetPlayerTabCompleter;
import com.whymertech.worldresetondeath.tabCompleters.RoleTabCompleter;

import com.whymertech.worldresetondeath.roles.UndeadRole;

import org.bukkit.event.Listener;


import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;


/*
 * worldresetondeath java plugin
 */
public class Plugin extends JavaPlugin implements Listener
{
    //public static final String WORLD_NAME = "world";
    //public static final String TEMP_WORLD_NAME = "lobby";
    //public static final long RECREATE_DELAY = 100L; // 1 second delay
    //public static final long SEED = -950547527103331411L;
    private File deathLogFile;

    private GameManager gameManager;
    private SeedManager seedManager;
    Utils utils = new Utils(this);


    @Override
    public void onEnable() {
        initDeathLogFile();
        
        gameManager = new GameManager(this);
        seedManager = gameManager.getSeedManager();

        getServer().getPluginManager().registerEvents(this, this); // Registering the main class as the listener
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this, gameManager), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new DoubleJumpListener(this, gameManager), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new EnchantmentListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new LobbyItemListener(), this);
        getServer().getPluginManager().registerEvents(new MobDamageListener(gameManager), this);


        getServer().getPluginManager().registerEvents(new UndeadRole(gameManager), this);        
        
        getCommand("kys").setExecutor(new KysCommand(this, gameManager)); // Registering the kys command
        getCommand("join").setExecutor(new JoinCommand(this, gameManager)); // Registering the join command
        getCommand("lobby").setExecutor(new LobbyCommand(this, gameManager)); // Registering the lobby command
        getCommand("role").setExecutor(new RoleCommand(gameManager));   // Registering the role command
        getCommand("role").setTabCompleter(new RoleTabCompleter());

        getCommand("addseed").setExecutor(new AddSeedCommand(seedManager));   // Registering the addseed command
        getCommand("givelobbyitem").setExecutor(new GiveLobbyItemCommand());   // Registering the addseed command
        getCommand("resetplayer").setExecutor(new ResetPlayerCommand(this, gameManager));   // Registering the addseed command
        getCommand("resetplayer").setTabCompleter(new ResetPlayerTabCompleter());

        getLogger().info("WorldResetOnDeath plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldResetOnDeath plugin has been disabled!");
    }

    public File getDeathLogFile() {
        return deathLogFile;
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
}