package com.nuclearcore.commands;

import com.nuclearcore.economy.EconomyService;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChequeCommand implements CommandExecutor {
    private final EconomyService economyService;
    private final ItemFactory itemFactory;
    private final double tax;

    public ChequeCommand(EconomyService economyService, ItemFactory itemFactory, double tax) {
        this.economyService = economyService;
        this.itemFactory = itemFactory;
        this.tax = tax;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(TextUtil.parse("<red>Use /cheque <economia> <valor>"));
            return true;
        }
        EconomyType type;
        try {
            type = EconomyType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(TextUtil.parse("<red>Economia inválida."));
            return true;
        }
        double amount = Double.parseDouble(args[1]);
        double total = amount + (amount * tax);
        if (!economyService.withdraw(player.getUniqueId(), type, total)) {
            player.sendMessage(TextUtil.parse("<red>Saldo insuficiente para taxa."));
            return true;
        }
        player.getInventory().addItem(itemFactory.createCheque(type, amount));
        player.sendMessage(TextUtil.parse("<green>Cheque criado com taxa de " + (int) (tax * 100) + "%"));
        return true;
    }
}
