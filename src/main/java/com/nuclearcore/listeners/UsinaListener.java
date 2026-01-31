package com.nuclearcore.listeners;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.usina.UsinaService;
import com.nuclearcore.utils.TextUtil;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class UsinaListener implements Listener {
    private final JavaPlugin plugin;
    private final UsinaService usinaService;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final ItemKeys keys;

    public UsinaListener(JavaPlugin plugin, UsinaService usinaService, PlayerDataManager dataManager, EconomyService economyService, ItemKeys keys) {
        this.plugin = plugin;
        this.usinaService = usinaService;
        this.dataManager = dataManager;
        this.economyService = economyService;
        this.keys = keys;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.TNT) {
            return;
        }
        if (!usinaService.isUsinaTnt(block)) {
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(keys.isqueiro(), PersistentDataType.INTEGER)) {
            player.sendMessage(TextUtil.parse("<red>Você precisa do Isqueiro Evolutivo."));
            event.setCancelled(true);
            return;
        }
        if (usinaService.isOnCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(TextUtil.parse("<red>Aguarde o cooldown."));
            return;
        }
        usinaService.setCooldown(player);
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        double base = plugin.getConfig().getDouble("usina.tnt-toxina-base", 10.0);
        double toxinatorBonus = data.getToxinatorLevel() * plugin.getConfig().getDouble("usina.isqueiro.toxinator.toxina-bonus-per-level", 0.1);
        double amount = base * (1.0 + toxinatorBonus);
        economyService.deposit(player.getUniqueId(), EconomyType.TOXINA, amount);
        block.setType(Material.AIR, false);
        int respawnSeconds = plugin.getConfig().getInt("usina.tnt-respawn-seconds", 10);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            block.setType(Material.TNT, false);
            usinaService.markTnt(block);
        }, respawnSeconds * 20L);
        if (data.getAspiradorLevel() > 0) {
            double chance = data.getAspiradorLevel() * plugin.getConfig().getDouble("usina.isqueiro.aspirador.chance-per-level", 0.05);
            if (ThreadLocalRandom.current().nextDouble() <= chance) {
                for (Block neighbor : List.of(block.getRelative(1, 0, 0), block.getRelative(-1, 0, 0), block.getRelative(0, 0, 1), block.getRelative(0, 0, -1))) {
                    if (usinaService.isUsinaTnt(neighbor)) {
                        neighbor.setType(Material.AIR, false);
                        economyService.deposit(player.getUniqueId(), EconomyType.TOXINA, amount);
                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            neighbor.setType(Material.TNT, false);
                            usinaService.markTnt(neighbor);
                        }, respawnSeconds * 20L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.blockList().stream().anyMatch(usinaService::isUsinaTnt)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if (event.blockList().stream().anyMatch(usinaService::isUsinaTnt)) {
            event.setCancelled(true);
        }
    }
}
