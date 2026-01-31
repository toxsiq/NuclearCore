package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KeyCommand implements CommandExecutor {
    private final ItemFactory itemFactory;

    public KeyCommand(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
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
            player.sendMessage(TextUtil.parse("<red>Use /key <tipo>"));
            return true;
        }
        player.getInventory().addItem(itemFactory.createKey(args[0]));
        return true;
    }
}
