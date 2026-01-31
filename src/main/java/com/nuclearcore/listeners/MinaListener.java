package com.nuclearcore.listeners;

import com.nuclearcore.mina.MinaService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MinaListener implements Listener {
    private final MinaService minaService;

    public MinaListener(MinaService minaService) {
        this.minaService = minaService;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() == null || !player.getWorld().equals(event.getBlock().getWorld())) {
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.NETHERITE_PICKAXE) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !TextUtil.parse("<aqua>Picareta Evolutiva").equals(meta.displayName())) {
            return;
        }
        event.setDropItems(false);
        minaService.reward(player);
    }
}
