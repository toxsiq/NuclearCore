package com.nuclearcore.robos;

import java.util.UUID;
import org.bukkit.Location;

public record RoboInstance(UUID owner, String type, boolean supreme, Location location) {
}
