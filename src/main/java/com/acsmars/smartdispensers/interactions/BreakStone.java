package com.acsmars.smartdispensers.interactions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BreakStone implements Interaction{
    @Override
    public boolean validInteraction(ItemStack sourceItem, Block targetBlock) {
        if (!targetBlock.getType().equals(Material.AIR) && !targetBlock.getType().equals(Material.WATER) && !targetBlock.getType().equals(Material.LAVA)) {
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
        return InteractionType.BREAK_STONE;
    }
}
