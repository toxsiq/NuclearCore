package com.nuclearcore.commands;

import com.nuclearcore.bosses.BossService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossCommand implements CommandExecutor {
    private final BossService bossService;

    public BossCommand(BossService bossService) {
        this.bossService = bossService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        if (!sender.hasPermission("nuclearcore.admin")) {
            player.sendMessage(TextUtil.parse("<red>Sem permissão."));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(TextUtil.parse("<red>Use /boss <tipo>"));
            return true;
        }
        bossService.spawnBoss(player, args[0]);
        return true;
    }
}
