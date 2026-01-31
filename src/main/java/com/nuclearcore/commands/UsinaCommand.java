package com.nuclearcore.commands;

import com.nuclearcore.usina.UsinaService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UsinaCommand implements CommandExecutor {
    private final UsinaService usinaService;

    public UsinaCommand(UsinaService usinaService) {
        this.usinaService = usinaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("marktnt")) {
            if (!player.hasPermission("nuclearcore.admin")) {
                player.sendMessage(TextUtil.parse("<red>Sem permissão."));
                return true;
            }
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.TNT) {
                player.sendMessage(TextUtil.parse("<red>Aponte para uma TNT."));
                return true;
            }
            usinaService.markTnt(block);
            player.sendMessage(TextUtil.parse("<green>TNT marcada."));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("unmarktnt")) {
            if (!player.hasPermission("nuclearcore.admin")) {
                player.sendMessage(TextUtil.parse("<red>Sem permissão."));
                return true;
            }
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.TNT) {
                player.sendMessage(TextUtil.parse("<red>Aponte para uma TNT."));
                return true;
            }
            usinaService.unmarkTnt(block);
            player.sendMessage(TextUtil.parse("<green>TNT desmarcada."));
            return true;
        }
        Location location = player.getLocation();
        if (usinaService.isInside(location)) {
            player.sendMessage(TextUtil.parse("<yellow>Você já está na Usina."));
            return true;
        }
        player.sendMessage(TextUtil.parse("<green>Teleportando para a Usina."));
        Location teleport = usinaService.getTeleportLocation();
        if (teleport != null) {
            player.teleport(teleport);
        }
        return true;
    }
}
