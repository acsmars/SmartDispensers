package com.acsmars.smartdispensers.interactions;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class PlantCrop implements Interaction{
    @Override
    public boolean validInteraction(ItemStack sourceItem, Block targetBlock) {
        return false;
    }

    @Override
    public boolean performInteraction(ItemStack sourceItem, Block targetBlock) {
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.PLANT_CROP;
    }
}
