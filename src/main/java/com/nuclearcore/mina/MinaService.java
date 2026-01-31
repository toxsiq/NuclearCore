package com.nuclearcore.mina;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.prestiges.PrestigeService;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinaService {
    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final PrestigeService prestigeService;
    private Location minaLocation;

    public MinaService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService, PrestigeService prestigeService) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.economyService = economyService;
        this.prestigeService = prestigeService;
        loadLocation();
    }

    private void loadLocation() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("mina.world", "world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        double x = config.getDouble("mina.location.x");
        double y = config.getDouble("mina.location.y");
        double z = config.getDouble("mina.location.z");
        minaLocation = new Location(world, x, y, z);
    }

    public void teleport(Player player) {
        if (minaLocation != null) {
            player.teleport(minaLocation);
        }
    }

    public void reward(Player player) {
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        double baseMoney = plugin.getConfig().getDouble("mina.reward-money", 5.0);
        double baseTokens = plugin.getConfig().getDouble("mina.reward-tokens", 1.0);
        double multiplier = prestigeService.getMineMoneyTokenMultiplier(data.getRankIndex(), data.getPrestigeLevel());
        economyService.deposit(player.getUniqueId(), EconomyType.MONEY, baseMoney * multiplier);
        economyService.deposit(player.getUniqueId(), EconomyType.TOKENS, baseTokens * multiplier);
    }
}
