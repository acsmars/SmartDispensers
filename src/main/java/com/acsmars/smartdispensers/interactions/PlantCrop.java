package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

public class PlantCrop implements Interaction {
    final Map<Material, List<Material>> cropRequirements = Map.ofEntries(
            entry(Material.OAK_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.SPRUCE_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.BIRCH_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.JUNGLE_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.ACACIA_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.DARK_OAK_SAPLING, Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.FARMLAND)),
            entry(Material.WHEAT_SEEDS, List.of(Material.FARMLAND)),
            entry(Material.PUMPKIN_SEEDS, List.of(Material.FARMLAND)),
            entry(Material.MELON_SEEDS, List.of(Material.FARMLAND)),
            entry(Material.BEETROOT_SEEDS, List.of(Material.FARMLAND)),
            entry(Material.POTATO, List.of(Material.FARMLAND)),
            entry(Material.NETHER_WART, List.of(Material.SOUL_SAND))
    );

    final Map<Material, Material> plantsAs = Map.ofEntries(
            entry(Material.OAK_SAPLING, Material.OAK_SAPLING),
            entry(Material.SPRUCE_SAPLING, Material.SPRUCE_SAPLING),
            entry(Material.BIRCH_SAPLING, Material.BIRCH_SAPLING),
            entry(Material.JUNGLE_SAPLING, Material.JUNGLE_SAPLING),
            entry(Material.ACACIA_SAPLING, Material.ACACIA_SAPLING),
            entry(Material.DARK_OAK_SAPLING, Material.DARK_OAK_SAPLING),
            entry(Material.WHEAT_SEEDS, Material.WHEAT),
            entry(Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM),
            entry(Material.MELON_SEEDS, Material.MELON_STEM),
            entry(Material.BEETROOT_SEEDS, Material.BEETROOTS),
            entry(Material.POTATO, Material.POTATOES),
            entry(Material.NETHER_WART, Material.NETHER_WART)
    );

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return cropRequirements.containsKey(sourceItem.getType());
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Block blockToPlantOn = null;
        List<Material> allowedFarmland = cropRequirements.get(sourceItem.getType());
        Block belowBlock = targetBlock.getRelative(BlockFace.DOWN);
        if (allowedFarmland.contains(targetBlock.getType())) {
            blockToPlantOn = targetBlock;
        }
        else if (allowedFarmland.contains(belowBlock.getType())) {
            blockToPlantOn = belowBlock;
        }

        if (blockToPlantOn != null) {
            Block plantSpace = blockToPlantOn.getRelative(BlockFace.UP);
            if (plantSpace.getType().equals(Material.AIR)) {
                plantSpace.setType(plantsAs.get(sourceItem.getType()));
                sourceItem.setAmount(sourceItem.getAmount() - 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.PLANT_CROP;
    }
}
