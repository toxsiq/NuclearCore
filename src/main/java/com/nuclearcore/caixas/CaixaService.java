package com.nuclearcore.caixas;

import com.nuclearcore.utils.RewardService;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CaixaService {
    private final JavaPlugin plugin;
    private final RewardService rewardService;
    private final File file;

    public CaixaService(JavaPlugin plugin, RewardService rewardService) {
        this.plugin = plugin;
        this.rewardService = rewardService;
        this.file = new File(plugin.getDataFolder(), "caixas.yml");
        if (!file.exists()) {
            plugin.saveResource("caixas.yml", false);
        }
    }

    public void open(Player player, String type) {
        rewardService.giveReward(player, file, "caixas." + type + ".rewards");
    }
}
