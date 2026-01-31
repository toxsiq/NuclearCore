package com.nuclearcore.pesca;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PescaService {
    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private Location fishingLocation;

    public PescaService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.economyService = economyService;
        loadLocation();
    }

    private void loadLocation() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("pesca.world", "world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        double x = config.getDouble("pesca.location.x");
        double y = config.getDouble("pesca.location.y");
        double z = config.getDouble("pesca.location.z");
        fishingLocation = new Location(world, x, y, z);
    }

    public void teleport(Player player) {
        if (fishingLocation != null) {
            player.teleport(fishingLocation);
        }
    }

    public void reward(Player player) {
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        double base = plugin.getConfig().getDouble("pesca.base-fish", 2.0);
        double multiplier = 1.0 + (data.getFishingLevel() * 0.1);
        economyService.deposit(player.getUniqueId(), EconomyType.PEIXES, base * multiplier);
    }
}
