package com.nuclearcore.listeners;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.items.SkinDisplayResolver;
import com.nuclearcore.items.SkinType;
import com.nuclearcore.menus.IsqueiroMenu;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class IsqueiroListener implements Listener {
    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final SkinDisplayResolver skinDisplayResolver;
    private final ItemKeys keys;
    private final IsqueiroMenu menu;

    public IsqueiroListener(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService, SkinDisplayResolver skinDisplayResolver, ItemKeys keys) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.economyService = economyService;
        this.skinDisplayResolver = skinDisplayResolver;
        this.keys = keys;
        this.menu = new IsqueiroMenu();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().isSneaking()) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.FLINT_AND_STEEL) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(keys.isqueiro(), PersistentDataType.INTEGER)) {
            return;
        }
        Player player = event.getPlayer();
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        player.openInventory(menu.create(data, skinDisplayResolver));
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().title() == null) {
            return;
        }
        if (!event.getView().title().equals(TextUtil.parse("<green>Isqueiro Evolutivo"))) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        FileConfiguration config = plugin.getConfig();
        switch (clicked.getType()) {
            case EMERALD -> {
                int level = data.getUraniumLevel();
                int max = config.getInt("usina.isqueiro.uranium.max-level", 10);
                if (level >= max) {
                    player.sendMessage(TextUtil.parse("<red>Uranium no nível máximo."));
                    return;
                }
                double cost = config.getDouble("usina.isqueiro.uranium.cost-toxina", 100.0);
                if (!economyService.withdraw(player.getUniqueId(), EconomyType.TOXINA, cost)) {
                    player.sendMessage(TextUtil.parse("<red>Você não tem toxina suficiente."));
                    return;
                }
                data.setUraniumLevel(level + 1);
            }
            case SLIME_BALL -> {
                int level = data.getToxinatorLevel();
                int max = config.getInt("usina.isqueiro.toxinator.max-level", 10);
                if (level >= max) {
                    player.sendMessage(TextUtil.parse("<red>Toxinator no nível máximo."));
                    return;
                }
                double cost = config.getDouble("usina.isqueiro.toxinator.cost-radiacao", 50.0);
                if (!economyService.withdraw(player.getUniqueId(), EconomyType.RADIACAO, cost)) {
                    player.sendMessage(TextUtil.parse("<red>Você não tem radiação suficiente."));
                    return;
                }
                data.setToxinatorLevel(level + 1);
            }
            case HOPPER -> {
                int level = data.getAspiradorLevel();
                int max = config.getInt("usina.isqueiro.aspirador.max-level", 5);
                if (level >= max) {
                    player.sendMessage(TextUtil.parse("<red>Aspirador no nível máximo."));
                    return;
                }
                double cost = config.getDouble("usina.isqueiro.aspirador.cost-radiacao", 150.0);
                if (!economyService.withdraw(player.getUniqueId(), EconomyType.RADIACAO, cost)) {
                    player.sendMessage(TextUtil.parse("<red>Você não tem radiação suficiente."));
                    return;
                }
                data.setAspiradorLevel(level + 1);
            }
            case PAPER -> {
                ItemMeta meta = clicked.getItemMeta();
                if (meta == null || meta.displayName() == null) {
                    return;
                }
                for (SkinType skin : SkinType.values()) {
                    if (meta.displayName().equals(TextUtil.parse(skinDisplayResolver.display(skin)))) {
                        if (!data.getUnlockedSkins().contains(skin)) {
                            player.sendMessage(TextUtil.parse("<red>Skin bloqueada."));
                            return;
                        }
                        data.setActiveSkin(skin);
                        break;
                    }
                }
            }
            default -> {
                return;
            }
        }
        player.openInventory(menu.create(data, skinDisplayResolver));
    }
}
