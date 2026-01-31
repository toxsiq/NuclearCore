package com.nuclearcore.listeners;

import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.keys.KeyService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class KeyListener implements Listener {
    private final KeyService keyService;
    private final ItemKeys keys;

    public KeyListener(KeyService keyService, ItemKeys keys) {
        this.keyService = keyService;
        this.keys = keys;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String type = meta.getPersistentDataContainer().get(keys.keyItem(), PersistentDataType.STRING);
        if (type == null) {
            return;
        }
        Player player = event.getPlayer();
        keyService.open(player, type);
        item.setAmount(item.getAmount() - 1);
        player.sendMessage(TextUtil.parse("<green>Chave usada."));
        event.setCancelled(true);
    }
}
