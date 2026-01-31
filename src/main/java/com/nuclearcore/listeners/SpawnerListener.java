package com.nuclearcore.listeners;

import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.spawners.SpawnerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerListener implements Listener {
    private final SpawnerService spawnerService;
    private final ItemKeys keys;

    public SpawnerListener(SpawnerService spawnerService, ItemKeys keys) {
        this.spawnerService = spawnerService;
        this.keys = keys;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String type = meta.getPersistentDataContainer().get(keys.spawner(), PersistentDataType.STRING);
        if (type == null) {
            return;
        }
        spawnerService.addSpawner(event.getPlayer().getUniqueId(), type, event.getBlockPlaced().getLocation());
    }
}
