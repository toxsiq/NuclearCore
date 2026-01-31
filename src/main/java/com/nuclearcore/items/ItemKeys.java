package com.nuclearcore.items;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemKeys {
    private final NamespacedKey isqueiro;
    private final NamespacedKey luckyblock;
    private final NamespacedKey keyItem;
    private final NamespacedKey cheque;
    private final NamespacedKey spawner;
    private final NamespacedKey robo;

    public ItemKeys(JavaPlugin plugin) {
        this.isqueiro = new NamespacedKey(plugin, "isqueiro");
        this.luckyblock = new NamespacedKey(plugin, "luckyblock");
        this.keyItem = new NamespacedKey(plugin, "key_item");
        this.cheque = new NamespacedKey(plugin, "cheque");
        this.spawner = new NamespacedKey(plugin, "spawner");
        this.robo = new NamespacedKey(plugin, "robo");
    }

    public NamespacedKey isqueiro() {
        return isqueiro;
    }

    public NamespacedKey luckyblock() {
        return luckyblock;
    }

    public NamespacedKey keyItem() {
        return keyItem;
    }

    public NamespacedKey cheque() {
        return cheque;
    }

    public NamespacedKey spawner() {
        return spawner;
    }

    public NamespacedKey robo() {
        return robo;
    }
}
