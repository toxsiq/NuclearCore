package com.nuclearcore.menus;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.items.SkinDisplayResolver;
import com.nuclearcore.items.SkinType;
import com.nuclearcore.utils.TextUtil;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IsqueiroMenu {
    public Inventory create(PlayerData data, SkinDisplayResolver skinDisplayResolver) {
        Inventory inventory = Bukkit.createInventory(null, 27, TextUtil.parse("<green>Isqueiro Evolutivo"));

        inventory.setItem(10, createMenuItem(Material.EMERALD, "<green>Uranium", List.of(
                "<gray>Nível: <white>" + data.getUraniumLevel(),
                "<gray>+Radiação por ciclo"
        )));
        inventory.setItem(12, createMenuItem(Material.SLIME_BALL, "<green>Toxinator", List.of(
                "<gray>Nível: <white>" + data.getToxinatorLevel(),
                "<gray>+Toxina por TNT"
        )));
        inventory.setItem(14, createMenuItem(Material.HOPPER, "<green>Aspirador", List.of(
                "<gray>Nível: <white>" + data.getAspiradorLevel(),
                "<gray>Chance de quebrar conjunto"
        )));
        inventory.setItem(16, createMenuItem(Material.NETHER_STAR, "<yellow>Skins", List.of(
                "<gray>Skin ativa: " + skinDisplayResolver.display(data.getActiveSkin())
        )));
        int slot = 18;
        for (SkinType skin : SkinType.values()) {
            ItemStack skinItem = createMenuItem(Material.PAPER, skinDisplayResolver.display(skin), List.of(
                    data.getUnlockedSkins().contains(skin)
                            ? "<green>Desbloqueada"
                            : "<red>Bloqueada"
            ));
            inventory.setItem(slot++, skinItem);
        }

        return inventory;
    }

    private ItemStack createMenuItem(Material material, String name, List<String> loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse(name));
        meta.lore(loreLines.stream().map(TextUtil::parse).toList());
        item.setItemMeta(meta);
        return item;
    }
}
