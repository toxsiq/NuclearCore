package com.nuclearcore.listeners;

import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ChequeListener implements Listener {
    private final EconomyService economyService;
    private final ItemKeys keys;

    public ChequeListener(EconomyService economyService, ItemKeys keys) {
        this.economyService = economyService;
        this.keys = keys;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getItemMeta() == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        String data = meta.getPersistentDataContainer().get(keys.cheque(), PersistentDataType.STRING);
        if (data == null) {
            return;
        }
        String[] parts = data.split(";");
        if (parts.length != 2) {
            return;
        }
        EconomyType type = EconomyType.valueOf(parts[0]);
        double amount = Double.parseDouble(parts[1]);
        Player player = event.getPlayer();
        economyService.deposit(player.getUniqueId(), type, amount);
        item.setAmount(item.getAmount() - 1);
        player.sendMessage(TextUtil.parse("<green>Cheque resgatado."));
        event.setCancelled(true);
    }
}
