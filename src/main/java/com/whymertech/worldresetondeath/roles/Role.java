package com.whymertech.worldresetondeath.roles;

import org.bukkit.Location;

public interface Role {
    Location getRoleSpawnLocation();
    void preparePlayer();
    void resetPlayer();
    boolean canDoubleJump();
    String name();
}
