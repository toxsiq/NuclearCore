package com.nuclearcore.prestiges;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PrestigeService {
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final Map<Integer, Double> prestigeCosts = new HashMap<>();

    public PrestigeService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService) {
        this.dataManager = dataManager;
        this.economyService = economyService;
        load(plugin);
    }

    private void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "prestiges.yml");
        if (!file.exists()) {
            plugin.saveResource("prestiges.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        prestigeCosts.clear();
        if (config.isConfigurationSection("prestiges")) {
            for (String key : config.getConfigurationSection("prestiges").getKeys(false)) {
                int level = Integer.parseInt(key);
                double cost = config.getDouble("prestiges." + key + ".cost.toxina", 0.0);
                prestigeCosts.put(level, cost);
            }
        }
    }

    public double getPrestigeCost(int nextLevel) {
        return prestigeCosts.getOrDefault(nextLevel, 0.0);
    }

    public boolean canPrestige(PlayerData data) {
        return data.getRankIndex() >= 25;
    }

    public boolean prestige(PlayerData data) {
        if (!canPrestige(data)) {
            return false;
        }
        int next = data.getPrestigeLevel() + 1;
        double cost = getPrestigeCost(next);
        if (!economyService.has(data.getUuid(), EconomyType.TOXINA, cost)) {
            return false;
        }
        economyService.withdraw(data.getUuid(), EconomyType.TOXINA, cost);
        data.setPrestigeLevel(next);
        data.setRankIndex(0);
        return true;
    }

    public double getMineMoneyTokenMultiplier(int rankIndex, int prestigeLevel) {
        double rankBonus = rankIndex * 0.10;
        double prestigeRankBonus = rankBonus * Math.pow(2, prestigeLevel);
        double prestigeMultiplier = Math.pow(2, prestigeLevel);
        return (1.0 + prestigeRankBonus) * prestigeMultiplier;
    }
}
