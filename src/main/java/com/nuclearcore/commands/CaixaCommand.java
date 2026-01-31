package com.nuclearcore.commands;

import com.nuclearcore.caixas.CaixaService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaixaCommand implements CommandExecutor {
    private final CaixaService caixaService;

    public CaixaCommand(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(TextUtil.parse("<red>Use /caixa <tipo>"));
            return true;
        }
        caixaService.open(player, args[0]);
        player.sendMessage(TextUtil.parse("<green>Caixa aberta."));
        return true;
    }
}
