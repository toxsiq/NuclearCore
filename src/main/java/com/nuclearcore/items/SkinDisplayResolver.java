package com.nuclearcore.items;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SkinDisplayResolver {
    private final JavaPlugin plugin;

    public SkinDisplayResolver(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String display(SkinType type) {
        FileConfiguration config = plugin.getConfig();
        String key = "skins." + type.name().toLowerCase();
        return config.getString(key + ".display", type.name());
    }

    public double bonus(SkinType type) {
        FileConfiguration config = plugin.getConfig();
        String key = "skins." + type.name().toLowerCase();
        return config.getDouble(key + ".bonus", 0.0);
    }
}
