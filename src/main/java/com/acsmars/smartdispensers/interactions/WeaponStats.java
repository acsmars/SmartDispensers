package com.acsmars.smartdispensers.interactions;


import lombok.AllArgsConstructor;
import org.bukkit.Material;

import java.util.Optional;

@AllArgsConstructor
public enum WeaponStats {
    WOOD_SWORD(Material.WOODEN_SWORD, 4),
    STONE_SWORD(Material.STONE_SWORD, 5),
    IRON_SWORD(Material.IRON_SWORD, 6),
    GOLDEN_SWORD(Material.GOLDEN_SWORD, 4),
    DIAMOND_SWORD(Material.DIAMOND_SWORD, 7),
    NETHERITE_SWORD(Material.NETHERITE_SWORD, 8),
    WOOD_AXE(Material.WOODEN_AXE, 7),
    STONE_AXE(Material.STONE_AXE, 9),
    IRON_AXE(Material.IRON_AXE, 9),
    GOLDEN_AXE(Material.GOLDEN_AXE, 7),
    DIAMOND_AXE(Material.DIAMOND_AXE, 9),
    NETHERITE_AXE(Material.NETHERITE_AXE, 10);

    public final Material material;

    public final int damage;

    public static Optional<WeaponStats> fromMaterial(Material material) {
        for (WeaponStats weaponStats : WeaponStats.values()) {
            if (weaponStats.material.equals(material)) {
                return Optional.of(weaponStats);
            }
        }
        return Optional.empty();
    }
}
