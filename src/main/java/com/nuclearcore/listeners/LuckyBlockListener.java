package com.nuclearcore.listeners;

import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.luckyblocks.LuckyBlockService;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LuckyBlockListener implements Listener {
    private final LuckyBlockService luckyBlockService;
    private final ItemKeys keys;

    public LuckyBlockListener(LuckyBlockService luckyBlockService, ItemKeys keys) {
        this.luckyBlockService = luckyBlockService;
        this.keys = keys;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String type = meta.getPersistentDataContainer().get(keys.luckyblock(), PersistentDataType.STRING);
        if (type == null) {
            return;
        }
        BlockState state = event.getBlockPlaced().getState();
        if (state instanceof TileState tileState) {
            tileState.getPersistentDataContainer().set(keys.luckyblock(), PersistentDataType.STRING, type);
            tileState.update(true, false);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (!(state instanceof TileState tileState)) {
            return;
        }
        PersistentDataContainer container = tileState.getPersistentDataContainer();
        String type = container.get(keys.luckyblock(), PersistentDataType.STRING);
        if (type == null) {
            return;
        }
        event.setDropItems(false);
        luckyBlockService.open(event.getPlayer(), type);
    }
}
