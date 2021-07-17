package com.acsmars.smartdispensers.interactions;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface Interaction {
    boolean validInteraction (ItemStack sourceItem, Block targetBlock);
    boolean performInteraction(ItemStack sourceItem, Block targetBlock);
    InteractionType getInteractionType();
}
