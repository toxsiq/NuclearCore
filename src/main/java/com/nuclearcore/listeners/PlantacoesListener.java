package com.nuclearcore.listeners;

import com.nuclearcore.plantacoes.PlantacoesService;
import com.nuclearcore.utils.TextUtil;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class PlantacoesListener implements Listener {
    private final PlantacoesService plantacoesService;
    private final Set<Material> crops;

    public PlantacoesListener(JavaPlugin plugin, PlantacoesService plantacoesService) {
        this.plantacoesService = plantacoesService;
        FileConfiguration config = plugin.getConfig();
        this.crops = config.getStringList("plantacoes.crops").stream()
                .map(Material::valueOf)
                .collect(Collectors.toSet());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!crops.contains(event.getBlock().getType())) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool.getType() != Material.NETHERITE_AXE) {
            event.setCancelled(true);
            return;
        }
        ItemMeta meta = tool.getItemMeta();
        if (meta == null || !TextUtil.parse("<green>Machado Farm Evolutivo").equals(meta.displayName())) {
            event.setCancelled(true);
            return;
        }
        Material cropType = event.getBlock().getType();
        event.setDropItems(false);
        event.getBlock().setType(cropType, false);
        plantacoesService.reward(player);
    }
}
