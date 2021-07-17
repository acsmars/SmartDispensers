package com.acsmars.smartdispensers.interactions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class PlaceBlock implements Interaction{

    @Override
    public boolean validInteraction(ItemStack sourceItem, Block targetBlock) {
        if (targetBlock.getType().equals(Material.WATER) || targetBlock.getType().equals(Material.LAVA) || targetBlock.getType().equals(Material.AIR)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean performInteraction(ItemStack sourceItem, Block targetBlock) {
        if (validInteraction(sourceItem, targetBlock)) {
            targetBlock.setType(sourceItem.getType());
            sourceItem.setAmount(sourceItem.getAmount()-1);
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.PLACE_BLOCK;
    }
}
