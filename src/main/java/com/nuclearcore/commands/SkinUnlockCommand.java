package com.nuclearcore.commands;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.items.SkinType;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkinUnlockCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;

    public SkinUnlockCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nuclearcore.admin")) {
            sender.sendMessage(TextUtil.parse("<red>Sem permissão."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("/skinunlock <player> <skin>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(TextUtil.parse("<red>Jogador offline."));
            return true;
        }
        SkinType skin = SkinType.valueOf(args[1].toUpperCase());
        PlayerData data = dataManager.getOrLoad(target.getUniqueId());
        if (data.getUnlockedSkins().contains(skin)) {
            sender.sendMessage(TextUtil.parse("<yellow>Skin já desbloqueada."));
            return true;
        }
        data.getUnlockedSkins().add(skin);
        sender.sendMessage(TextUtil.parse("<green>Skin desbloqueada."));
        return true;
    }
}
