package com.whymertech.worldresetondeath.roles;

import org.bukkit.Location;
import org.bukkit.Material;

public interface Role {
    Location getRoleSpawnLocation();
    void preparePlayer();
    void resetPlayer();
    boolean canDoubleJump();
    String name();
    void addEffects();
    Material favoriteFood();
}
