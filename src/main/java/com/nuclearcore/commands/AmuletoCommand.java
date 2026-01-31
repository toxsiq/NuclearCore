package com.nuclearcore.commands;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmuletoCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;

    public AmuletoCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                PlayerData data = dataManager.getOrLoad(player.getUniqueId());
                player.sendMessage(TextUtil.parse("<yellow>Amuleto nível: <white>" + data.getAmuletLevel()));
                player.sendMessage(TextUtil.parse("<yellow>Amuleto universal: <white>" + (data.hasUniversalAmulet() ? "Sim" : "Não")));
            }
            return true;
        }
        if (!sender.hasPermission("nuclearcore.admin")) {
            sender.sendMessage(TextUtil.parse("<red>Sem permissão."));
            return true;
        }
        if (args[0].equalsIgnoreCase("set") && args.length >= 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(TextUtil.parse("<red>Jogador offline."));
                return true;
            }
            int level = Integer.parseInt(args[2]);
            PlayerData data = dataManager.getOrLoad(target.getUniqueId());
            data.setAmuletLevel(level);
            sender.sendMessage(TextUtil.parse("<green>Amuleto definido."));
            return true;
        }
        if (args[0].equalsIgnoreCase("universal") && args.length >= 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(TextUtil.parse("<red>Jogador offline."));
                return true;
            }
            PlayerData data = dataManager.getOrLoad(target.getUniqueId());
            data.setUniversalAmulet(true);
            sender.sendMessage(TextUtil.parse("<green>Amuleto universal ativado."));
            return true;
        }
        return true;
    }
}
