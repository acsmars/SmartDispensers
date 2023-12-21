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

public class TillSoil implements Interaction {

    final List<Material> hoes = Arrays.asList(Material.WOODEN_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE);
    final List<Material> tillAble = Arrays.asList(Material.GRASS_BLOCK, Material.DIRT, Material.DIRT_PATH);

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return hoes.contains(sourceItem.getType());
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        Block belowBlock = targetBlock.getRelative(BlockFace.DOWN);
        Block blockToTill = null;
        if (tillAble.contains(targetBlock.getType())) {
            blockToTill = targetBlock;
        } else if (tillAble.contains(belowBlock.getType())) {
            blockToTill = belowBlock;
        }
        if (blockToTill != null) {
            blockToTill.setType(Material.FARMLAND);
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.TILL_SOIL;
    }
}
