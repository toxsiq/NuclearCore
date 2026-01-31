package com.nuclearcore.items;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.utils.TextUtil;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemFactory {
    private final ItemKeys keys;
    private final SkinDisplayResolver skinDisplayResolver;

    public ItemFactory(ItemKeys keys, SkinDisplayResolver skinDisplayResolver) {
        this.keys = keys;
        this.skinDisplayResolver = skinDisplayResolver;
    }

    public ItemStack createIsqueiro(PlayerData data) {
        ItemStack item = new ItemStack(Material.FLINT_AND_STEEL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<green>Isqueiro Evolutivo <gray>[" + skinDisplayResolver.display(data.getActiveSkin()) + "<gray>]"));
        meta.lore(List.of(
                TextUtil.parse("<gray>Uranium: <green>" + data.getUraniumLevel()),
                TextUtil.parse("<gray>Toxinator: <green>" + data.getToxinatorLevel()),
                TextUtil.parse("<gray>Aspirador: <green>" + data.getAspiradorLevel()),
                TextUtil.parse("<gray>Skin ativa: " + skinDisplayResolver.display(data.getActiveSkin()))
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(keys.isqueiro(), PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createPickaxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<aqua>Picareta Evolutiva"));
        meta.lore(List.of(TextUtil.parse("<gray>Ferramenta oficial da Mina.")));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createFarmAxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<green>Machado Farm Evolutivo"));
        meta.lore(List.of(TextUtil.parse("<gray>Ferramenta oficial das Plantações.")));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createFishingRod() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<blue>Vara de Pesca Evolutiva"));
        meta.lore(List.of(TextUtil.parse("<gray>Pesque para ganhar Peixes.")));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createLuckyBlock(String type) {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<gold>LuckyBlock " + type));
        meta.getPersistentDataContainer().set(keys.luckyblock(), PersistentDataType.STRING, type);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createKey(String type) {
        ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<yellow>Chave " + type));
        meta.getPersistentDataContainer().set(keys.keyItem(), PersistentDataType.STRING, type);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createCheque(EconomyType economyType, double amount) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<green>Cheque de " + economyType.name()));
        meta.lore(List.of(TextUtil.parse("<gray>Valor: <white>" + amount)));
        meta.getPersistentDataContainer().set(keys.cheque(), PersistentDataType.STRING, economyType.name() + ";" + amount);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSpawner(String type) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(TextUtil.parse("<gold>Spawner de " + type));
        meta.getPersistentDataContainer().set(keys.spawner(), PersistentDataType.STRING, type);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createRobo(String type, boolean supreme) {
        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        ItemMeta meta = item.getItemMeta();
        String name = supreme ? "Robô Supremo" : "Robô";
        meta.displayName(TextUtil.parse("<red>" + name + " de " + type));
        meta.getPersistentDataContainer().set(keys.robo(), PersistentDataType.STRING, type + ";" + (supreme ? "supreme" : "normal"));
        item.setItemMeta(meta);
        return item;
    }
}
