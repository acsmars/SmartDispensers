package com.acsmars.smartdispensers.interactions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreakWooden implements Interaction {

    private final List<Material> breakableMaterials = Arrays.asList(
            Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG,
            Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.CRIMSON_PLANKS, Material.DARK_OAK_PLANKS, Material.JUNGLE_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.WARPED_PLANKS,
            Material.CRIMSON_ROOTS, Material.CRIMSON_STEM, Material.WARPED_ROOTS, Material.WARPED_STEM);


    @Override
    public boolean validInteraction(ItemStack sourceItem, Block targetBlock) {
        if (breakableMaterials.contains(targetBlock.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean performInteraction(ItemStack sourceItem, Block targetBlock) {
        if (validInteraction(sourceItem, targetBlock)) {
            targetBlock.breakNaturally();
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.BREAK_WOODEN;
    }
}
