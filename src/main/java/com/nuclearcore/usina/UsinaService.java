package com.nuclearcore.usina;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.SkinDisplayResolver;
import com.nuclearcore.items.SkinType;
import com.nuclearcore.utils.Cuboid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class UsinaService {
    private final JavaPlugin plugin;
    private final PlayerDataManager dataManager;
    private final EconomyService economyService;
    private final SkinDisplayResolver skinDisplayResolver;
    private final NamespacedKey tntKey;
    private Cuboid region;
    private Location teleportLocation;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public UsinaService(JavaPlugin plugin, PlayerDataManager dataManager, EconomyService economyService, SkinDisplayResolver skinDisplayResolver) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.economyService = economyService;
        this.skinDisplayResolver = skinDisplayResolver;
        this.tntKey = new NamespacedKey(plugin, "usina_tnt");
        loadRegion();
        startRadiationTask();
    }

    private void loadRegion() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("usina.world", "world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        int minX = config.getInt("usina.min.x");
        int minY = config.getInt("usina.min.y");
        int minZ = config.getInt("usina.min.z");
        int maxX = config.getInt("usina.max.x");
        int maxY = config.getInt("usina.max.y");
        int maxZ = config.getInt("usina.max.z");
        region = new Cuboid(world,
                Math.min(minX, maxX),
                Math.min(minY, maxY),
                Math.min(minZ, maxZ),
                Math.max(minX, maxX),
                Math.max(minY, maxY),
                Math.max(minZ, maxZ));
        double tX = config.getDouble("usina.teleport.x");
        double tY = config.getDouble("usina.teleport.y");
        double tZ = config.getDouble("usina.teleport.z");
        teleportLocation = new Location(world, tX, tY, tZ);
    }

    public boolean isInside(Location location) {
        return region != null && region.contains(location);
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public void markTnt(Block block) {
        if (block.getType() != Material.TNT) {
            return;
        }
        Chunk chunk = block.getChunk();
        Set<String> marks = getChunkMarks(chunk);
        marks.add(serialize(block));
        storeChunkMarks(chunk, marks);
    }

    public void unmarkTnt(Block block) {
        if (block.getType() != Material.TNT) {
            return;
        }
        Chunk chunk = block.getChunk();
        Set<String> marks = getChunkMarks(chunk);
        marks.remove(serialize(block));
        storeChunkMarks(chunk, marks);
    }

    public boolean isUsinaTnt(Block block) {
        if (block.getType() != Material.TNT) {
            return false;
        }
        if (!isInside(block.getLocation())) {
            return false;
        }
        Chunk chunk = block.getChunk();
        Set<String> marks = getChunkMarks(chunk);
        return marks.contains(serialize(block));
    }

    public boolean isOnCooldown(Player player) {
        long now = System.currentTimeMillis();
        long last = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long cooldownMs = plugin.getConfig().getInt("usina.tnt-cooldown-seconds", 2) * 1000L;
        return (now - last) < cooldownMs;
    }

    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void startRadiationTask() {
        int interval = 20 * 5;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!isInside(player.getLocation())) {
                    continue;
                }
                PlayerData data = dataManager.getOrLoad(player.getUniqueId());
                double base = plugin.getConfig().getDouble("usina.radiation-base", 5.0);
                double rankBonus = plugin.getConfig().getDouble("usina.radiation-rank-bonus", 0.02) * data.getRankIndex();
                double prestigeBonus = plugin.getConfig().getDouble("usina.radiation-prestige-bonus", 0.1) * data.getPrestigeLevel();
                double uraniumBonus = data.getUraniumLevel() * plugin.getConfig().getDouble("usina.isqueiro.uranium.radiation-bonus-per-level", 0.05);
                SkinType skin = data.getActiveSkin();
                double skinBonus = skinDisplayResolver.bonus(skin);
                double total = base * (1.0 + rankBonus + prestigeBonus + uraniumBonus + skinBonus);
                economyService.deposit(player.getUniqueId(), EconomyType.RADIACAO, total);
            }
        }, interval, interval);
    }

    private Set<String> getChunkMarks(Chunk chunk) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        String raw = container.get(tntKey, PersistentDataType.STRING);
        Set<String> marks = new HashSet<>();
        if (raw == null || raw.isBlank()) {
            return marks;
        }
        for (String entry : raw.split("\\|")) {
            if (!entry.isBlank()) {
                marks.add(entry);
            }
        }
        return marks;
    }

    private void storeChunkMarks(Chunk chunk, Set<String> marks) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (marks.isEmpty()) {
            container.remove(tntKey);
            return;
        }
        container.set(tntKey, PersistentDataType.STRING, String.join("|", marks));
    }

    private String serialize(Block block) {
        return block.getX() + "," + block.getY() + "," + block.getZ();
    }
}
