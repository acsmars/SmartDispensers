package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import com.acsmars.smartdispensers.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

import static org.bukkit.Bukkit.getLogger;

public class MilkCow implements Interaction {

    private final long minMilkingDelay = 15000L;

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return (sourceItem.getType() == Material.BUCKET && targetBlock.getType() != Material.WATER && targetBlock.getType() != Material.LAVA);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<Cow> possibleCow = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1, x -> x.getType() == EntityType.COW)
                .stream().map(x -> (Cow) x).filter(x -> isMilkable(plugin, x)).findFirst();
        if (possibleCow.isPresent()) {

            // We've milked a cow, now we need to put the milk bucket somewhere.
            sourceItem.setAmount(sourceItem.getAmount() - 1); // If this is an event item, it'll get removed by the scheduler instead
            markMilked(plugin, possibleCow.get());
            // Check for a chest with inventory space below the cow
            Block belowBlock = targetBlock.getRelative(BlockFace.DOWN);
            if (belowBlock.getType().equals(Material.CHEST)) {
                Chest chest = (Chest) belowBlock.getState();
                Inventory chestInventory = chest.getInventory();
                if (Util.inventoryHasSpace(chestInventory)) {
                    chestInventory.addItem(new ItemStack(Material.MILK_BUCKET));
                    return true;
                }
            }
            // There was no chest with space for the bucket.
            try {
                Location dropLocation = targetBlock.getLocation();
                dropLocation.getWorld().dropItemNaturally(dropLocation, new ItemStack(Material.MILK_BUCKET));
                return true;
            } catch (NullPointerException npe) {
            }
        }
        getLogger().info("No milkable cow");
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.MILK_COW;
    }

    private boolean isMilkable(Plugin plugin, Cow cow) {
        List<MetadataValue> cowMetadataValues = cow.getMetadata("dispenserMilked");
        if (cowMetadataValues.size() > 0) {
            long lastMilkedTimestamp = cowMetadataValues.get(0).asLong();
            return System.currentTimeMillis() >= lastMilkedTimestamp + minMilkingDelay;
        }
        return true;
    }

    /**
     * Timestamp this cow with the current time it was milked.
     * @param cow
     */
    private void markMilked(Plugin plugin, Cow cow) {
        cow.setMetadata("dispenserMilked", new FixedMetadataValue(plugin, System.currentTimeMillis()));
    }
}
