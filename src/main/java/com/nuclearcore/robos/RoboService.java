package com.nuclearcore.robos;

import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.storage.RoboStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class RoboService {
    private final JavaPlugin plugin;
    private final EconomyService economyService;
    private final RoboStorage storage;
    private final List<RoboInstance> robos = new ArrayList<>();

    public RoboService(JavaPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
        this.storage = new RoboStorage(plugin);
        this.robos.addAll(storage.load());
        startTask();
    }

    public boolean canPlaceSupreme(UUID owner, String type) {
        return robos.stream().noneMatch(robo -> robo.owner().equals(owner)
                && robo.type().equalsIgnoreCase(type)
                && robo.supreme());
    }

    public void addRobo(UUID owner, String type, boolean supreme, Location location) {
        robos.add(new RoboInstance(owner, type, supreme, location));
        saveAsync();
    }

    private void startTask() {
        long interval = plugin.getConfig().getInt("robos.reward-interval-seconds", 60) * 20L;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (RoboInstance robo : robos) {
                double base = switch (robo.type().toLowerCase()) {
                    case "toxina" -> plugin.getConfig().getDouble("robos.base-reward.toxina", 10.0);
                    case "tokens" -> plugin.getConfig().getDouble("robos.base-reward.tokens", 5.0);
                    default -> plugin.getConfig().getDouble("robos.base-reward.cash", 50.0);
                };
                double reward = robo.supreme() ? base * 100 : base;
                EconomyType economy = switch (robo.type().toLowerCase()) {
                    case "toxina" -> EconomyType.TOXINA;
                    case "tokens" -> EconomyType.TOKENS;
                    default -> EconomyType.CASH;
                };
                economyService.deposit(robo.owner(), economy, reward);
            }
        }, interval, interval);
    }

    private void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                storage.save(robos);
            } catch (IOException e) {
                plugin.getLogger().warning("Falha ao salvar robos: " + e.getMessage());
            }
        });
    }
}
