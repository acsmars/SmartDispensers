package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class IgniteMob extends InteractionImpl implements Interaction{

    private final int mobRange = 1;

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return (sourceItem.getType().equals(Material.FLINT_AND_STEEL) && targetBlock.getType() != Material.WATER && targetBlock.getType() != Material.LAVA);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<LivingEntity> possibleEntity = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), mobRange, mobRange, mobRange, x -> x instanceof LivingEntity)
                .stream().map(x -> (LivingEntity) x).filter(x -> (!(x instanceof Creeper)) || !((Creeper) x).isPowered()).findFirst();
        if (possibleEntity.isPresent()) {
            // We have a horse with no saddle. Let's put a saddle on it.
            LivingEntity entity = possibleEntity.get();
            if (entity instanceof Creeper) {
                Creeper creeper = (Creeper) entity;
                creeper.ignite();
            }
            else {
                int newTicks = entity.getFireTicks() + 80;
                entity.setFireTicks(newTicks);
            }
            if (Math.abs(random.nextInt() % 4) < 1) {
                Dispenser dispenser = (Dispenser) event.getBlock().getState();
                damageItem(plugin, dispenser.getInventory(), sourceItem);
            }
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.IGNITE_MOB;
    }
}
