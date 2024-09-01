package com.whymertech.worldresetondeath.listeners;

import com.whymertech.worldresetondeath.GameManager;

import org.bukkit.event.Listener;

public class AnvilListener implements Listener {

    GameManager gameManager;

    public AnvilListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
