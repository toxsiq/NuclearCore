package com.nuclearcore.utils;

import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RewardService {
    private final JavaPlugin plugin;
    private final EconomyService economyService;

    public RewardService(JavaPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
    }

    public void giveReward(Player player, File file, String sectionPath) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isList(sectionPath)) {
            return;
        }
        List<RewardDefinition> rewards = new ArrayList<>();
        for (var entry : config.getMapList(sectionPath)) {
            String type = String.valueOf(entry.get("type"));
            String economy = entry.get("economy") != null ? String.valueOf(entry.get("economy")) : null;
            String material = entry.get("material") != null ? String.valueOf(entry.get("material")) : null;
            double amount = Double.parseDouble(String.valueOf(entry.get("amount")));
            double chance = Double.parseDouble(String.valueOf(entry.get("chance")));
            rewards.add(new RewardDefinition(type, economy, material, amount, chance));
        }
        double roll = ThreadLocalRandom.current().nextDouble();
        double cumulative = 0.0;
        for (RewardDefinition reward : rewards) {
            cumulative += reward.chance();
            if (roll <= cumulative) {
                if ("economy".equalsIgnoreCase(reward.type())) {
                    EconomyType type = EconomyType.valueOf(reward.economy());
                    economyService.deposit(player.getUniqueId(), type, reward.amount());
                } else if ("item".equalsIgnoreCase(reward.type())) {
                    Material material = Material.valueOf(reward.material());
                    ItemStack item = new ItemStack(material, (int) reward.amount());
                    player.getInventory().addItem(item);
                }
                return;
            }
        }
    }
}
