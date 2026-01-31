package com.nuclearcore.commands;

import com.nuclearcore.items.ItemFactory;
import com.nuclearcore.items.ItemKeys;
import com.nuclearcore.robos.RoboService;
import com.nuclearcore.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class RoboCommand implements CommandExecutor {
    private final ItemFactory itemFactory;
    private final RoboService roboService;
    private final ItemKeys keys;

    public RoboCommand(ItemFactory itemFactory, RoboService roboService, ItemKeys keys) {
        this.itemFactory = itemFactory;
        this.roboService = roboService;
        this.keys = keys;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(TextUtil.parse("<red>Use /robo give|place"));
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("nuclearcore.admin")) {
                player.sendMessage(TextUtil.parse("<red>Sem permissão."));
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(TextUtil.parse("<red>Use /robo give <cash|toxina|tokens> [supreme]"));
                return true;
            }
            boolean supreme = args.length > 2 && args[2].equalsIgnoreCase("supreme");
            player.getInventory().addItem(itemFactory.createRobo(args[1], supreme));
            return true;
        }
        if (args[0].equalsIgnoreCase("place")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return true;
            }
            String data = meta.getPersistentDataContainer().get(keys.robo(), PersistentDataType.STRING);
            if (data == null) {
                player.sendMessage(TextUtil.parse("<red>Segure um robô válido."));
                return true;
            }
            String[] parts = data.split(";");
            String type = parts[0];
            boolean supreme = parts.length > 1 && parts[1].equalsIgnoreCase("supreme");
            if (supreme && !roboService.canPlaceSupreme(player.getUniqueId(), type)) {
                player.sendMessage(TextUtil.parse("<red>Você já possui um robô supremo desse tipo."));
                return true;
            }
            roboService.addRobo(player.getUniqueId(), type, supreme, player.getLocation());
            item.setAmount(item.getAmount() - 1);
            player.sendMessage(TextUtil.parse("<green>Robô colocado."));
            return true;
        }
        return true;
    }
}
