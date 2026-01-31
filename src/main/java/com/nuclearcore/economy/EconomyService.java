package com.nuclearcore.economy;

import com.nuclearcore.data.PlayerData;
import com.nuclearcore.data.PlayerDataManager;
import java.util.UUID;

public class EconomyService {
    private final PlayerDataManager dataManager;

    public EconomyService(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public double getBalance(UUID uuid, EconomyType type) {
        PlayerData data = dataManager.getOrLoad(uuid);
        return data.getBalance(type);
    }

    public void setBalance(UUID uuid, EconomyType type, double amount) {
        PlayerData data = dataManager.getOrLoad(uuid);
        data.setBalance(type, amount);
    }

    public boolean has(UUID uuid, EconomyType type, double amount) {
        return getBalance(uuid, type) >= amount;
    }

    public boolean withdraw(UUID uuid, EconomyType type, double amount) {
        if (!has(uuid, type, amount)) {
            return false;
        }
        setBalance(uuid, type, getBalance(uuid, type) - amount);
        return true;
    }

    public void deposit(UUID uuid, EconomyType type, double amount) {
        setBalance(uuid, type, getBalance(uuid, type) + amount);
    }
}
