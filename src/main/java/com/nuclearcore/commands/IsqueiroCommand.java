package com.nuclearcore.commands;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsqueiroCommand implements CommandExecutor {
    private final PlayerDataManager dataManager;
    private final ItemFactory itemFactory;

    public IsqueiroCommand(PlayerDataManager dataManager, ItemFactory itemFactory) {
        this.dataManager = dataManager;
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        PlayerData data = dataManager.getOrLoad(player.getUniqueId());
        player.getInventory().addItem(itemFactory.createIsqueiro(data));
        player.sendMessage(TextUtil.parse("<green>Isqueiro recebido."));
        return true;
    }
}
