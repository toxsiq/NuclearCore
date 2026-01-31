package com.nuclearcore.rankup;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RankService {
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final List<RankDefinition> ranks = new ArrayList<>();

    public RankService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService) {
        this.dataManager = dataManager;
        this.economyService = economyService;
        load(plugin);
    }

    private void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "rankup.yml");
        if (!file.exists()) {
            plugin.saveResource("rankup.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ranks.clear();
        for (char c = 'A'; c <= 'Z'; c++) {
            String key = "ranks." + c;
            double money = config.getDouble(key + ".cost.money", 0.0);
            double tokens = config.getDouble(key + ".cost.tokens", 0.0);
            ranks.add(new RankDefinition(String.valueOf(c), money, tokens));
        }
    }

    public String getRankName(int index) {
        if (index < 0 || index >= ranks.size()) {
            return "A";
        }
        return ranks.get(index).name();
    }

    public double getRankBonus(int rankIndex) {
        return rankIndex * 0.10;
    }

    public boolean canRankUp(PlayerData data) {
        return data.getRankIndex() < ranks.size() - 1;
    }

    public RankDefinition getNextRank(PlayerData data) {
        int next = Math.min(ranks.size() - 1, data.getRankIndex() + 1);
        return ranks.get(next);
    }

    public boolean rankUp(PlayerData data) {
        if (!canRankUp(data)) {
            return false;
        }
        RankDefinition next = getNextRank(data);
        if (!economyService.has(data.getUuid(), EconomyType.MONEY, next.moneyCost())
                || !economyService.has(data.getUuid(), EconomyType.TOKENS, next.tokensCost())) {
            return false;
        }
        economyService.withdraw(data.getUuid(), EconomyType.MONEY, next.moneyCost());
        economyService.withdraw(data.getUuid(), EconomyType.TOKENS, next.tokensCost());
        data.setRankIndex(data.getRankIndex() + 1);
        return true;
    }

    public record RankDefinition(String name, double moneyCost, double tokensCost) {
    }
}
