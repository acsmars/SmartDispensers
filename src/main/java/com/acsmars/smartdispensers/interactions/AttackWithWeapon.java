package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

import static org.bukkit.Bukkit.getLogger;

public class AttackWithWeapon implements Interaction{
    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        getLogger().info("" + sourceItem.getType());
        return WeaponStats.fromMaterial(sourceItem.getType()).isPresent();
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<LivingEntity> possibleVictim = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1, x -> x instanceof LivingEntity)
                .stream().map(x -> (LivingEntity) x).findFirst();
        getLogger().info("Victims: " + possibleVictim);

        if (possibleVictim.isPresent() && WeaponStats.fromMaterial(sourceItem.getType()).isPresent()) {
            LivingEntity victim = possibleVictim.get();
            // Find a player to attribute this damage to
            Player operator = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 20, 20, 20, x -> x instanceof Player).stream().map(x -> (Player) x).findFirst().orElse(null);
            victim.damage(WeaponStats.fromMaterial(sourceItem.getType()).get().damage, operator);
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.ATTACK_WITH_WEAPON;
    }
}
