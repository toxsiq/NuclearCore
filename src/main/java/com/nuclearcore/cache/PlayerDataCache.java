package com.nuclearcore.cache;

import com.nuclearcore.data.PlayerData;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataCache {
    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public void put(PlayerData data) {
        cache.put(data.getUuid(), data);
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
    }

    public Map<UUID, PlayerData> all() {
        return cache;
    }
}
