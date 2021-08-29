package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class LightningWithTrident extends InteractionImpl implements Interaction{

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return sourceItem.getType().equals(Material.TRIDENT) && sourceItem.getEnchantments().containsKey(Enchantment.CHANNELING);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.LIGHTNING_WITH_TRIDENT;
    }
}
