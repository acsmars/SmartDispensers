package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import com.acsmars.smartdispensers.Util;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class EquipSaddle implements Interaction{

    private final int horseRange = 1;

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return (sourceItem.getType().equals(Material.SHEARS) && targetBlock.getType() != Material.WATER && targetBlock.getType() != Material.LAVA);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<Horse> possibleHorse = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), horseRange, horseRange, horseRange, x -> (x.getType() == EntityType.HORSE || x.getType() == EntityType.SKELETON_HORSE))
                .stream().map(x -> (Horse) x).filter(x -> x.getInventory().getSaddle() == null).findFirst();
        if (possibleHorse.isPresent()) {
            // We have a horse with no saddle. Let's put a saddle on it.
            Horse horse = possibleHorse.get();
            horse.getInventory().setSaddle(sourceItem);
            sourceItem.setAmount(sourceItem.getAmount()-1);
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.EQUIP_SADDLE;
    }
}
