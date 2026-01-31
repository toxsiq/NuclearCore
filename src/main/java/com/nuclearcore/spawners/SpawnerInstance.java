package com.nuclearcore.spawners;

import java.util.UUID;
import org.bukkit.Location;

public record SpawnerInstance(UUID owner, String type, Location location) {
}
