package com.nuclearcore.data;

import com.nuclearcore.cache.PlayerDataCache;
import com.nuclearcore.storage.YamlPlayerStorage;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataManager {
    private final JavaPlugin plugin;
    private final PlayerDataCache cache;
    private final YamlPlayerStorage storage;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cache = new PlayerDataCache();
        this.storage = new YamlPlayerStorage(plugin);
    }

    public PlayerData getOrLoad(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            return data;
        }
        PlayerData loaded = storage.load(uuid);
        cache.put(loaded);
        return loaded;
    }

    public void unload(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            saveAsync(data);
            cache.remove(uuid);
        }
    }

    public void saveAll() {
        cache.all().values().forEach(this::saveAsync);
    }

    public void saveAsync(PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                storage.save(data);
            } catch (IOException e) {
                plugin.getLogger().warning("Falha ao salvar dados de " + data.getUuid() + ": " + e.getMessage());
            }
        });
    }

    public void handleJoin(Player player) {
        getOrLoad(player.getUniqueId());
    }

    public void handleQuit(Player player) {
        unload(player.getUniqueId());
    }
}
