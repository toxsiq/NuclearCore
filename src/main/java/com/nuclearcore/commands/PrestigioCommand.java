package com.nuclearcore.commands;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.prestiges.PrestigeService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrestigioCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;
    private final PrestigeService prestigeService;

    public PrestigioCommand(PlayerDataManager dataManager, PrestigeService prestigeService) {
        this.dataManager = dataManager;
        this.prestigeService = prestigeService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        if (!prestigeService.canPrestige(data)) {
            player.sendMessage(TextUtil.parse("<red>Alcance o rank Z para prestigiar."));
            return true;
        }
        if (!prestigeService.prestige(data)) {
            player.sendMessage(TextUtil.parse("<red>Você não possui toxina suficiente."));
            return true;
        }
        player.sendMessage(TextUtil.parse("<green>Prestígio realizado! Novo nível: <white>" + data.getPrestigeLevel()));
        return true;
    }
}
