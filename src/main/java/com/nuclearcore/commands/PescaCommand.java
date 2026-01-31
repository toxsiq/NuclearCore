package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.pesca.PescaService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PescaCommand implements CommandExecutor {
    private final PescaService pescaService;
    private final ItemFactory itemFactory;

    public PescaCommand(PescaService pescaService, ItemFactory itemFactory) {
        this.pescaService = pescaService;
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        pescaService.teleport(player);
        player.getInventory().addItem(itemFactory.createFishingRod());
        player.sendMessage(TextUtil.parse("<green>Boa pesca!"));
        return true;
    }
}
