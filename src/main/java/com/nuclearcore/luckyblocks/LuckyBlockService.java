package com.nuclearcore.luckyblocks;

import com.nuclearcore.utils.RewardService;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyBlockService {
    private final JavaPlugin plugin;
    private final RewardService rewardService;
    private final File file;

    public LuckyBlockService(JavaPlugin plugin, RewardService rewardService) {
        this.plugin = plugin;
        this.rewardService = rewardService;
        this.file = new File(plugin.getDataFolder(), "luckyblocks.yml");
        if (!file.exists()) {
            plugin.saveResource("luckyblocks.yml", false);
        }
    }

    public void open(Player player, String type) {
        rewardService.giveReward(player, file, "luckyblocks." + type + ".rewards");
    }
}
