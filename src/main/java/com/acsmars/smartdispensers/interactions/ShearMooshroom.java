package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import com.acsmars.smartdispensers.Util;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class ShearMooshroom extends InteractionImpl implements Interaction {
    private final int range = 1;
    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return sourceItem.getType().equals(Material.SHEARS);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<MushroomCow> possibleMushroomCow = targetBlock.getWorld()
                .getNearbyEntities(targetBlock.getLocation(), range, range, range, x -> x.getType() == EntityType.MOOSHROOM)
                .stream().map(x -> (MushroomCow) x)
                .filter(Breedable::canBreed)
                .findFirst();
        if (possibleMushroomCow.isPresent()) {
            // We've milked a cow, now we need to put the milk bucket somewhere.
            MushroomCow mushroomCow = possibleMushroomCow.get();
            ItemStack mushroom = new ItemStack(mushroomCow.getVariant().equals(MushroomCow.Variant.RED) ? Material.RED_MUSHROOM : Material.BROWN_MUSHROOM, 5);

            // Delete the mooshroom and create a replacement cow
            World world = mushroomCow.getWorld();
            Location location = mushroomCow.getLocation();
            mushroomCow.remove();
            world.spawnEntity(location, EntityType.COW);

            // Damage the shears
            if (Math.abs(random.nextInt() % 4) < 1) {
                Dispenser dispenser = (Dispenser) event.getBlock().getState();
                damageItem(plugin, dispenser.getInventory(), sourceItem);
            }

            // Check for a chest with inventory space below the cow
            Block chestOutput = event.getBlock().getRelative(BlockFace.DOWN);
            if (chestOutput.getType().equals(Material.CHEST)) {
                Chest chest = (Chest) chestOutput.getState();
                Inventory chestInventory = chest.getInventory();
                if (Util.inventoryHasSpace(chestInventory)) {
                    chestInventory.addItem(mushroom);
                    return true;
                }
            }
            // There was no chest with space for the bucket.
            try {
                Location dropLocation = targetBlock.getLocation();
                dropLocation.getWorld().dropItemNaturally(dropLocation, mushroom);
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
