package com.nuclearcore.utils;

import org.bukkit.Location;
import org.bukkit.World;

public record Cuboid(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public boolean contains(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        if (!location.getWorld().getName().equals(world.getName())) {
            return false;
        }
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }
}
