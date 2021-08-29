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
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static java.util.Map.entry;

public class ShearSheep extends InteractionImpl implements Interaction  {

    private final int sheepRange = 1;

    private Map<DyeColor, Material> dyeToWool = Map.ofEntries(
        entry(DyeColor.WHITE, Material.WHITE_WOOL),
        entry(DyeColor.ORANGE, Material.ORANGE_WOOL),
            entry(DyeColor.MAGENTA, Material.MAGENTA_WOOL),
            entry(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_WOOL),
            entry(DyeColor.YELLOW, Material.YELLOW_WOOL),
            entry(DyeColor.LIME, Material.LIME_WOOL),
            entry(DyeColor.PINK, Material.PINK_WOOL),
            entry(DyeColor.GRAY, Material.GRAY_WOOL),
            entry(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_WOOL),
            entry(DyeColor.CYAN, Material.CYAN_WOOL),
            entry(DyeColor.PURPLE, Material.PURPLE_WOOL),
            entry(DyeColor.BLUE, Material.BLUE_WOOL),
            entry(DyeColor.BROWN, Material.BROWN_WOOL),
            entry(DyeColor.GREEN, Material.GREEN_WOOL),
            entry(DyeColor.RED, Material.RED_WOOL),
            entry(DyeColor.BLACK, Material.BLACK_WOOL)
    );

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return (sourceItem.getType().equals(Material.SHEARS) && targetBlock.getType() != Material.WATER && targetBlock.getType() != Material.LAVA);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<Sheep> possibleSheep = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), sheepRange, sheepRange, sheepRange, x -> x.getType() == EntityType.SHEEP)
                .stream().map(x -> (Sheep) x).filter(x -> !x.isSheared()).findFirst();
        if (possibleSheep.isPresent()) {
            // We've milked a cow, now we need to put the milk bucket somewhere.
            Sheep sheep = possibleSheep.get();
            DyeColor woolColor = sheep.getColor();
            ItemStack wool = new ItemStack(dyeToWool.get(woolColor), (Math.abs(random.nextInt() % 3)) + 1);

            if (Math.abs(random.nextInt() % 4) < 1) {
                Dispenser dispenser = (Dispenser) event.getBlock().getState();
                damageItem(plugin, dispenser.getInventory(), sourceItem);
            }
            sheep.setSheared(true);
            // Check for a chest with inventory space below the cow
            Block chestOutput = event.getBlock().getRelative(BlockFace.DOWN);
            if (chestOutput.getType().equals(Material.CHEST)) {
                Chest chest = (Chest) chestOutput.getState();
                Inventory chestInventory = chest.getInventory();
                if (Util.inventoryHasSpace(chestInventory)) {
                    chestInventory.addItem(wool);
                    return true;
                }
            }
            // There was no chest with space for the bucket.
            try {
                Location dropLocation = targetBlock.getLocation();
                dropLocation.getWorld().dropItemNaturally(dropLocation, wool);
                return true;
            } catch (NullPointerException npe) {
            }
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.SHEAR_SHEEP;
    }
}
