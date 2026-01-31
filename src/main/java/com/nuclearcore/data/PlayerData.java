package com.nuclearcore.data;

import com.nuclearcore.economy.EconomyType;
import com.nuclearcore.items.SkinType;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private final Map<EconomyType, Double> balances = new EnumMap<>(EconomyType.class);
    private int rankIndex;
    private int prestigeLevel;
    private int uraniumLevel;
    private int toxinatorLevel;
    private int aspiradorLevel;
    private SkinType activeSkin = SkinType.REATORA;
    private final Set<SkinType> unlockedSkins = EnumSet.of(SkinType.REATORA);
    private int fishingLevel;
    private int farmAxeLevel;
    private int amuletLevel;
    private boolean universalAmulet;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        for (EconomyType type : EconomyType.values()) {
            balances.put(type, 0.0);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getBalance(EconomyType type) {
        return balances.getOrDefault(type, 0.0);
    }

    public void setBalance(EconomyType type, double amount) {
        balances.put(type, Math.max(0.0, amount));
    }

    public int getRankIndex() {
        return rankIndex;
    }

    public void setRankIndex(int rankIndex) {
        this.rankIndex = Math.max(0, Math.min(rankIndex, 25));
    }

    public int getPrestigeLevel() {
        return prestigeLevel;
    }

    public void setPrestigeLevel(int prestigeLevel) {
        this.prestigeLevel = Math.max(0, prestigeLevel);
    }

    public int getUraniumLevel() {
        return uraniumLevel;
    }

    public void setUraniumLevel(int uraniumLevel) {
        this.uraniumLevel = Math.max(0, uraniumLevel);
    }

    public int getToxinatorLevel() {
        return toxinatorLevel;
    }

    public void setToxinatorLevel(int toxinatorLevel) {
        this.toxinatorLevel = Math.max(0, toxinatorLevel);
    }

    public int getAspiradorLevel() {
        return aspiradorLevel;
    }

    public void setAspiradorLevel(int aspiradorLevel) {
        this.aspiradorLevel = Math.max(0, aspiradorLevel);
    }

    public SkinType getActiveSkin() {
        return activeSkin;
    }

    public void setActiveSkin(SkinType activeSkin) {
        this.activeSkin = activeSkin;
    }

    public Set<SkinType> getUnlockedSkins() {
        return unlockedSkins;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public void setFishingLevel(int fishingLevel) {
        this.fishingLevel = Math.max(0, fishingLevel);
    }

    public int getFarmAxeLevel() {
        return farmAxeLevel;
    }

    public void setFarmAxeLevel(int farmAxeLevel) {
        this.farmAxeLevel = Math.max(0, farmAxeLevel);
    }

    public int getAmuletLevel() {
        return amuletLevel;
    }

    public void setAmuletLevel(int amuletLevel) {
        this.amuletLevel = Math.max(0, amuletLevel);
    }

    public boolean hasUniversalAmulet() {
        return universalAmulet;
    }

    public void setUniversalAmulet(boolean universalAmulet) {
        this.universalAmulet = universalAmulet;
    }
}
