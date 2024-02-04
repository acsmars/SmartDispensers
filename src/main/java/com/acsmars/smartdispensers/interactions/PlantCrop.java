package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.bukkit.Bukkit.getLogger;

public class PlantCrop implements Interaction {
    final Map<Material, List<MaterialSide>> cropRequirements = Map.ofEntries(
            entry(Material.OAK_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.SPRUCE_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.BIRCH_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.JUNGLE_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.ACACIA_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.DARK_OAK_SAPLING, List.of(
                    new MaterialSide(Material.GRASS_BLOCK),
                    new MaterialSide(Material.DIRT),
                    new MaterialSide(Material.COARSE_DIRT),
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.WHEAT_SEEDS, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.PUMPKIN_SEEDS, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.MELON_SEEDS, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.BEETROOT_SEEDS, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.POTATO, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.NETHER_WART, List.of(
                    new MaterialSide(Material.SOUL_SAND))),
            entry(Material.CARROT, List.of(
                    new MaterialSide(Material.FARMLAND))),
            entry(Material.COCOA_BEANS, List.of(
                    new MaterialSide(Material.JUNGLE_LOG, false),
                    new MaterialSide(Material.JUNGLE_WOOD)))
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
            entry(Material.NETHER_WART, Material.NETHER_WART),
            entry(Material.CARROT, Material.CARROTS),
            entry(Material.COCOA_BEANS, Material.COCOA)
    );

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return cropRequirements.containsKey(sourceItem.getType());
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {

        List<MaterialSide> cropRequiredMaterial = cropRequirements.get(sourceItem.getType());
        for (MaterialSide potentialMaterialSide : cropRequiredMaterial) {
            // Path for crops planted on top of blocks
            if (potentialMaterialSide.useTop) {
                Block blockToPlantOn = null;
                Block belowBlock = targetBlock.getRelative(BlockFace.DOWN);
                if (potentialMaterialSide.getMaterial() == targetBlock.getType()) {
                    blockToPlantOn = targetBlock;
                } else if (potentialMaterialSide.getMaterial() == belowBlock.getType()) {
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
            }
            // Path for crops planted on the side of blocks
            else if (targetBlock.getType() == Material.AIR) {
                Block blockToPlantOn = null;
                BlockFace blockFace = null;
                if (potentialMaterialSide.getMaterial() == targetBlock.getRelative(BlockFace.NORTH).getType()) {
                    blockToPlantOn = targetBlock.getRelative(BlockFace.NORTH);
                    blockFace = BlockFace.SOUTH;
                }
                else if (potentialMaterialSide.getMaterial() == targetBlock.getRelative(BlockFace.EAST).getType()) {
                    blockToPlantOn = targetBlock.getRelative(BlockFace.EAST);
                    blockFace = BlockFace.WEST;
                }
                else if (potentialMaterialSide.getMaterial() == targetBlock.getRelative(BlockFace.SOUTH).getType()) {
                    blockToPlantOn = targetBlock.getRelative(BlockFace.SOUTH);
                    blockFace = BlockFace.NORTH;
                }
                else if (potentialMaterialSide.getMaterial() == targetBlock.getRelative(BlockFace.WEST).getType()) {
                    blockToPlantOn = targetBlock.getRelative(BlockFace.WEST);
                    blockFace = BlockFace.EAST;
                }

                if (blockToPlantOn != null) {
                    Block plantSpace = blockToPlantOn.getRelative(blockFace);
                    if (plantSpace.getType().equals(Material.AIR)) {
                        Material newMaterial = plantsAs.get(sourceItem.getType());
                        plantSpace.setType(newMaterial);
                        sourceItem.setAmount(sourceItem.getAmount() - 1);

                        // Fix directions for the block if it's directional
                        if (plantSpace.getBlockData() instanceof Directional) {
                            Directional directional = (Directional) plantSpace.getBlockData();
                            directional.setFacing(blockFace.getOppositeFace());
                            plantSpace.setBlockData(directional);
                            plantSpace.getState().update();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Data
    private class MaterialSide {
        public Material material;
        // Otherwise the side will be used
        public boolean useTop;

        public MaterialSide(Material material) {
            this.material = material;
            this.useTop = true;
        }
        public MaterialSide(Material material, boolean useTop) {
            this.material = material;
            this.useTop = useTop;
        }
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.PLANT_CROP;
    }
}
