package com.nuclearcore.storage;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.SkinType;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlPlayerStorage {
    private final File folder;

    public YamlPlayerStorage(JavaPlugin plugin) {
        this.folder = new File(plugin.getDataFolder(), "players");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public PlayerData load(UUID uuid) {
        File file = new File(folder, uuid + ".yml");
        PlayerData data = new PlayerData(uuid);
        if (!file.exists()) {
            return data;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (EconomyType type : EconomyType.values()) {
            data.setBalance(type, config.getDouble("balances." + type.name(), 0.0));
        }
        data.setRankIndex(config.getInt("rank", 0));
        data.setPrestigeLevel(config.getInt("prestige", 0));
        data.setUraniumLevel(config.getInt("isqueiro.uranium", 0));
        data.setToxinatorLevel(config.getInt("isqueiro.toxinator", 0));
        data.setAspiradorLevel(config.getInt("isqueiro.aspirador", 0));
        data.setFishingLevel(config.getInt("pesca.level", 0));
        data.setFarmAxeLevel(config.getInt("plantacoes.farm-axe", 0));
        data.setAmuletLevel(config.getInt("plantacoes.amulet", 0));
        data.setUniversalAmulet(config.getBoolean("plantacoes.universal", false));
        String skin = config.getString("isqueiro.active-skin", SkinType.REATORA.name());
        data.setActiveSkin(SkinType.valueOf(skin));
        Set<SkinType> unlocked = EnumSet.of(SkinType.REATORA);
        for (String entry : config.getStringList("isqueiro.unlocked")) {
            try {
                unlocked.add(SkinType.valueOf(entry));
            } catch (IllegalArgumentException ignored) {
            }
        }
        data.getUnlockedSkins().clear();
        data.getUnlockedSkins().addAll(unlocked);
        return data;
    }

    public void save(PlayerData data) throws IOException {
        File file = new File(folder, data.getUuid() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        for (EconomyType type : EconomyType.values()) {
            config.set("balances." + type.name(), data.getBalance(type));
        }
        config.set("rank", data.getRankIndex());
        config.set("prestige", data.getPrestigeLevel());
        config.set("isqueiro.uranium", data.getUraniumLevel());
        config.set("isqueiro.toxinator", data.getToxinatorLevel());
        config.set("isqueiro.aspirador", data.getAspiradorLevel());
        config.set("isqueiro.active-skin", data.getActiveSkin().name());
        config.set("isqueiro.unlocked", data.getUnlockedSkins().stream().map(Enum::name).toList());
        config.set("pesca.level", data.getFishingLevel());
        config.set("plantacoes.farm-axe", data.getFarmAxeLevel());
        config.set("plantacoes.amulet", data.getAmuletLevel());
        config.set("plantacoes.universal", data.hasUniversalAmulet());
        config.save(file);
    }
}
