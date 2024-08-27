package com.whymertech.worldresetondeath.listeners;

import com.whymertech.worldresetondeath.roles.BlackSmithRole;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.event.Listener;
import com.whymertech.worldresetondeath.roles.Role;

public class AnvilListener implements Listener {

    GameManager gameManager;

    public AnvilListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
