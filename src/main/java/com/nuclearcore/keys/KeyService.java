package com.nuclearcore.keys;

import com.nuclearcore.utils.RewardService;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KeyService {
    private final JavaPlugin plugin;
    private final RewardService rewardService;
    private final File file;

    public KeyService(JavaPlugin plugin, RewardService rewardService) {
        this.plugin = plugin;
        this.rewardService = rewardService;
        this.file = new File(plugin.getDataFolder(), "keys.yml");
        if (!file.exists()) {
            plugin.saveResource("keys.yml", false);
        }
    }

    public void open(Player player, String type) {
        rewardService.giveReward(player, file, "keys." + type + ".rewards");
    }
}
