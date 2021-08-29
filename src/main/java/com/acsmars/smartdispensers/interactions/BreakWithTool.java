package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BreakWithTool implements Interaction {
    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        if (targetBlock.getType().equals(Material.WATER) || targetBlock.getType().equals(Material.LAVA) || targetBlock.getType().equals(Material.AIR)) {
            return false;
        }
        return targetBlock.isPreferredTool(sourceItem);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        targetBlock.breakNaturally(sourceItem);
        event.setCancelled(true);
        return true;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.BREAK_WITH_TOOL;
    }
}
