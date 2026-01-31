package com.nuclearcore.bosses;

import com.nuclearcore.utils.RewardService;
import java.io.File;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class BossService {
    private final JavaPlugin plugin;
    private final RewardService rewardService;
    private final File file;
    private final NamespacedKey bossKey;

    public BossService(JavaPlugin plugin, RewardService rewardService) {
        this.plugin = plugin;
        this.rewardService = rewardService;
        this.file = new File(plugin.getDataFolder(), "bosses.yml");
        if (!file.exists()) {
            plugin.saveResource("bosses.yml", false);
        }
        this.bossKey = new NamespacedKey(plugin, "boss_type");
    }

    public void spawnBoss(Player player, String type) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String entityName = config.getString("bosses." + type + ".entity", "WITHER");
        EntityType entityType = EntityType.valueOf(entityName);
        Entity entity = player.getWorld().spawnEntity(player.getLocation(), entityType);
        entity.getPersistentDataContainer().set(bossKey, PersistentDataType.STRING, type);
    }

    public boolean isBoss(Entity entity) {
        return entity.getPersistentDataContainer().has(bossKey, PersistentDataType.STRING);
    }

    public void reward(Player player, String type) {
        rewardService.giveReward(player, file, "bosses." + type + ".rewards");
    }

    public NamespacedKey bossKey() {
        return bossKey;
    }
}
