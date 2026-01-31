package com.nuclearcore.listeners;

import com.nuclearcore.bosses.BossService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class BossListener implements Listener {
    private final BossService bossService;

    public BossListener(BossService bossService) {
        this.bossService = bossService;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!bossService.isBoss(event.getEntity())) {
            return;
        }
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            return;
        }
        String type = event.getEntity().getPersistentDataContainer().get(bossService.bossKey(), PersistentDataType.STRING);
        if (type != null) {
            bossService.reward(killer, type);
        }
    }
}
