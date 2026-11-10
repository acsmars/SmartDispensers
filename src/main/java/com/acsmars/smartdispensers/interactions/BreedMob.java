package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import com.acsmars.smartdispensers.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import javax.sql.rowset.spi.SyncFactoryException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.sql.rowset.spi.SyncFactory.getLogger;

public class BreedMob implements Interaction {

    final List<Material> breedItems = List.of(Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE, Material.GOLDEN_CARROT, Material.WHEAT, Material.CARROT, Material.BEETROOT,
            Material.WHEAT_SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS, Material.BEETROOT_SEEDS, Material.TORCHFLOWER_SEEDS, Material.PITCHER_POD,
            Material.BEEF, Material.COOKED_BEEF, Material.CHICKEN, Material.COOKED_CHICKEN, Material.PORKCHOP, Material.COOKED_PORKCHOP,
            Material.MUTTON, Material.COOKED_MUTTON, Material.RABBIT, Material.COOKED_RABBIT, Material.ROTTEN_FLESH,
            Material.COD, Material.SALMON, Material.TROPICAL_FISH_BUCKET, Material.HAY_BLOCK, Material.DANDELION, Material.SEAGRASS,
            Material.BAMBOO, Material.SWEET_BERRIES, Material.GLOW_BERRIES, Material.WARPED_FUNGUS, Material.CRIMSON_FUNGUS,
            Material.SLIME_BALL, Material.CACTUS
            );

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return breedItems.contains(sourceItem.getType());
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Optional<Animals> possibleAnimal = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1)
                .stream()
                .filter(x -> (x instanceof Animals))
                .map(x -> (Animals) x)
                .filter(Animals::canBreed)
                .filter(x -> x.isBreedItem(sourceItem))
                .filter(x -> x.getLoveModeTicks() == 0)
                .findFirst();
        if (possibleAnimal.isPresent()) {
            sourceItem.setAmount(sourceItem.getAmount() - 1); // If this is an event item, it'll get removed by the scheduler instead
            possibleAnimal.get().setLoveModeTicks(600);
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.MILK_COW;
    }
}
