package com.nuclearcore.storage;

import com.nuclearcore.spawners.SpawnerInstance;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerStorage {
    private final File file;

    public SpawnerStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "spawners.yml");
    }

    public List<SpawnerInstance> load() {
        List<SpawnerInstance> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("spawners")) {
            return list;
        }
        for (String key : config.getConfigurationSection("spawners").getKeys(false)) {
            String base = "spawners." + key;
            UUID owner = UUID.fromString(config.getString(base + ".owner"));
            String type = config.getString(base + ".type");
            String worldName = config.getString(base + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }
            double x = config.getDouble(base + ".x");
            double y = config.getDouble(base + ".y");
            double z = config.getDouble(base + ".z");
            list.add(new SpawnerInstance(owner, type, new Location(world, x, y, z)));
        }
        return list;
    }

    public void save(List<SpawnerInstance> spawners) throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        int index = 0;
        for (SpawnerInstance instance : spawners) {
            String base = "spawners." + index++;
            config.set(base + ".owner", instance.owner().toString());
            config.set(base + ".type", instance.type());
            config.set(base + ".world", instance.location().getWorld().getName());
            config.set(base + ".x", instance.location().getX());
            config.set(base + ".y", instance.location().getY());
            config.set(base + ".z", instance.location().getZ());
        }
        config.save(file);
    }
}
