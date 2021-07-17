package com.acsmars.smartdispensers.interactions;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;

public class MilkCow implements Interaction{

    @Override
    public boolean validInteraction(ItemStack sourceItem, Block targetBlock) {
        return false;
    }

    @Override
    public boolean performInteraction(ItemStack sourceItem, Block targetBlock) {
        getLogger().info("failed to milk");
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.MILK_COW;
    }
}
