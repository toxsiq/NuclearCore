package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.mina.MinaService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinaCommand implements CommandExecutor {
    private final MinaService minaService;
    private final ItemFactory itemFactory;

    public MinaCommand(MinaService minaService, ItemFactory itemFactory) {
        this.minaService = minaService;
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        minaService.teleport(player);
        player.getInventory().addItem(itemFactory.createPickaxe());
        player.sendMessage(TextUtil.parse("<green>Bem-vindo à Mina."));
        return true;
    }
}
