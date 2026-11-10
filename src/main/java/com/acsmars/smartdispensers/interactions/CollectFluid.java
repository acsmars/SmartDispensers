package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import com.acsmars.smartdispensers.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class CollectFluid implements Interaction {

    final List<Material> fluidCauldrons = List.of(Material.WATER_CAULDRON, Material.LAVA_CAULDRON, Material.POWDER_SNOW_CAULDRON);
    final List<Material> fluids = List.of(Material.WATER, Material.LAVA, Material.POWDER_SNOW);

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return fluids.contains(targetBlock.getType()) || fluidCauldrons.contains(targetBlock.getType());
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<ItemStack> filledBucket = Optional.empty();
        if (fluids.contains(targetBlock.getType())) {
            switch (targetBlock.getType()) {
                case LAVA:
                    filledBucket = Optional.of(new ItemStack(Material.LAVA_BUCKET));
                    break;
                case POWDER_SNOW:
                    filledBucket = Optional.of(new ItemStack(Material.POWDER_SNOW_BUCKET));
                    break;
                default:
                    filledBucket = Optional.of(new ItemStack(Material.WATER_BUCKET));
            }
            targetBlock.setType(Material.AIR);
        } else if (fluidCauldrons.contains(targetBlock.getType())) {
            switch (targetBlock.getType()) {
                case LAVA_CAULDRON:
                    filledBucket = Optional.of(new ItemStack(Material.LAVA_BUCKET));
                    break;
                case POWDER_SNOW_CAULDRON:
                    filledBucket = Optional.of(new ItemStack(Material.POWDER_SNOW_BUCKET));
                    break;
                default:
                    filledBucket = Optional.of(new ItemStack(Material.WATER_BUCKET));
            }
            targetBlock.setType(Material.CAULDRON);
        }

        if (filledBucket.isPresent()) {
            sourceItem.setAmount(sourceItem.getAmount() - 1); // If this is an event item, it'll get removed by the scheduler instead
            // Check for a container with inventory space below the fluid
            Block belowBlock = targetBlock.getRelative(BlockFace.DOWN);
            if (belowBlock.getState() instanceof Container) {
                Container container = (Container) belowBlock.getState();
                Inventory containerInventory = container.getInventory();
                if (Util.inventoryHasSpace(containerInventory)) {
                    containerInventory.addItem(filledBucket.get());
                    return true;
                }
            } else {
                try {
                    Location dropLocation = targetBlock.getLocation();
                    dropLocation.getWorld().dropItemNaturally(dropLocation, filledBucket.get());
                    return true;
                } catch (NullPointerException npe) {
                }
            }
        }

        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.COLLECT_FLUID;
    }
}
