package com.nuclearcore.listeners;

import com.nuclearcore.pesca.PescaService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PescaListener implements Listener {
    private final PescaService pescaService;

    public PescaListener(PescaService pescaService) {
        this.pescaService = pescaService;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            pescaService.reward(event.getPlayer());
            event.setExpToDrop(0);
        }
    }
}
