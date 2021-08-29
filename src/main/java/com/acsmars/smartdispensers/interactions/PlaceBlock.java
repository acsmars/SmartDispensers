package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlaceBlock implements Interaction{

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        if (targetBlock.getType().equals(Material.WATER) || targetBlock.getType().equals(Material.LAVA) || targetBlock.getType().equals(Material.AIR)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        if (validInteraction(plugin, event, sourceItem, targetBlock)) {
            targetBlock.setType(sourceItem.getType());
            sourceItem.setAmount(sourceItem.getAmount()-1);
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.PLACE_BLOCK;
    }
}
