package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AttackWithWeapon implements Interaction{
    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return WeaponStats.fromMaterial(sourceItem.getType()).isPresent();
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        List<LivingEntity> possibleVictims = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1, x -> x instanceof LivingEntity)
                .stream().map(x -> (LivingEntity) x).limit(16).collect(Collectors.toList());

        if (WeaponStats.fromMaterial(sourceItem.getType()).isPresent()) {
            boolean damagedEntity = false;
            Player operator = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 20, 20, 20, x -> x instanceof Player).stream().map(x -> (Player) x).findFirst().orElse(null);
            for (LivingEntity victim: possibleVictims) {
                // Find a player to attribute this damage to
                victim.damage(WeaponStats.fromMaterial(sourceItem.getType()).get().damage, operator);
                damagedEntity = true;
            }
            return damagedEntity;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.ATTACK_WITH_WEAPON;
    }
}
