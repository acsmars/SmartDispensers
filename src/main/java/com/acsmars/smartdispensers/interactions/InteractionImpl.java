package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class InteractionImpl implements Interaction {

    public static Random random = new Random();

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return false;
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return false;
    }

    @Override
    public InteractionType getInteractionType() {
        return null;
    }

    public void damageItem(SmartDispensers plugin, Inventory inventory, ItemStack itemToDamage) {
        Material material = itemToDamage.getType();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack != null && itemStack.getType().equals(material)) {
                        if (itemStack.getItemMeta() instanceof Damageable) {
                            Damageable damageable = ((Damageable) itemStack.getItemMeta());
                            damageable.setDamage(damageable.getDamage() + 1);
                            itemStack.setItemMeta((ItemMeta) damageable);
                            if (damageable.getDamage() > material.getMaxDurability()) {
                                itemStack.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }, 2L);
    }
}
