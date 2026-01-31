package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerCommand implements CommandExecutor {
    private final ItemFactory itemFactory;

    public SpawnerCommand(ItemFactory itemFactory) {
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
            player.sendMessage(TextUtil.parse("<red>Use /spawner <coins|tokens>"));
            return true;
        }
        player.getInventory().addItem(itemFactory.createSpawner(args[0]));
        return true;
    }
}
