package com.nuclearcore.spawners;

import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.storage.SpawnerStorage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerService {
    private final JavaPlugin plugin;
    private final EconomyService economyService;
    private final SpawnerStorage storage;
    private final List<SpawnerInstance> spawners = new ArrayList<>();

    public SpawnerService(JavaPlugin plugin, EconomyService economyService) {
        this.plugin = plugin;
        this.economyService = economyService;
        this.storage = new SpawnerStorage(plugin);
        this.spawners.addAll(storage.load());
        startTask();
    }

    public void addSpawner(UUID owner, String type, Location location) {
        spawners.add(new SpawnerInstance(owner, type, location));
        saveAsync();
    }

    private void startTask() {
        long interval = plugin.getConfig().getInt("spawners.reward-interval-seconds", 60) * 20L;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (SpawnerInstance instance : spawners) {
                EconomyType economy = instance.type().equalsIgnoreCase("tokens") ? EconomyType.TOKENS : EconomyType.MONEY;
                double reward = instance.type().equalsIgnoreCase("tokens")
                        ? plugin.getConfig().getDouble("spawners.token-reward", 5.0)
                        : plugin.getConfig().getDouble("spawners.coin-reward", 20.0);
                economyService.deposit(instance.owner(), economy, reward);
            }
        }, interval, interval);
    }

    private void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                storage.save(spawners);
            } catch (IOException e) {
                plugin.getLogger().warning("Falha ao salvar spawners: " + e.getMessage());
            }
        });
    }
}
