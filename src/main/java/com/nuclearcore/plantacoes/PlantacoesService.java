package com.nuclearcore.plantacoes;

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

public class PlantacoesService {
    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private Location plotLocation;

    public PlantacoesService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.economyService = economyService;
        loadLocation();
    }

    private void loadLocation() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("plantacoes.world", "world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        double x = config.getDouble("plantacoes.location.x");
        double y = config.getDouble("plantacoes.location.y");
        double z = config.getDouble("plantacoes.location.z");
        plotLocation = new Location(world, x, y, z);
    }

    public void teleport(Player player) {
        if (plotLocation != null) {
            player.teleport(plotLocation);
        }
    }

    public void reward(Player player) {
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        double amount = plugin.getConfig().getDouble("plantacoes.reward-cash", 3.0);
        double amuletBonus = switch (data.getAmuletLevel()) {
            case 1 -> 0.25;
            case 2 -> 0.50;
            case 3 -> 1.0;
            default -> 0.0;
        };
        if (data.hasUniversalAmulet()) {
            amuletBonus += 0.25;
        }
        economyService.deposit(player.getUniqueId(), EconomyType.CASH, amount * (1.0 + amuletBonus));
    }
}
