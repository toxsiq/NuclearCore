package com.nuclearcore.commands;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.rankup.RankService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;
    private final RankService rankService;

    public RankupCommand(PlayerDataManager dataManager, RankService rankService) {
        this.dataManager = dataManager;
        this.rankService = rankService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        if (!rankService.canRankUp(data)) {
            player.sendMessage(TextUtil.parse("<red>Você já está no último rank."));
            return true;
        }
        if (!rankService.rankUp(data)) {
            player.sendMessage(TextUtil.parse("<red>Você não possui saldo suficiente."));
            return true;
        }
        player.sendMessage(TextUtil.parse("<green>Rankup realizado! Novo rank: <white>" + rankService.getRankName(data.getRankIndex())));
        return true;
    }
}
