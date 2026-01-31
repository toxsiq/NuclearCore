package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.plantacoes.PlantacoesService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmazemCommand implements CommandExecutor {
    private final PlantacoesService plantacoesService;
    private final ItemFactory itemFactory;

    public ArmazemCommand(PlantacoesService plantacoesService, ItemFactory itemFactory) {
        this.plantacoesService = plantacoesService;
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        plantacoesService.teleport(player);
        player.getInventory().addItem(itemFactory.createFarmAxe());
        player.sendMessage(TextUtil.parse("<green>Bem-vindo ao Armazém."));
        return true;
    }
}
