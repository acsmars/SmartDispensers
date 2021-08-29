package com.acsmars.smartdispensers;

import com.acsmars.smartdispensers.interactions.InteractionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public class DispenserListener implements Listener {

    Map<Material, List<InteractionType>> materialInteractions;

    SmartDispensers plugin;

    DispenserListener(SmartDispensers plugin, Map<Material, List<InteractionType>> materialInteractions) {
        this.plugin = plugin;
        this.materialInteractions = materialInteractions;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    private void onDispenserFire(BlockDispenseEvent event) {
        // Check dispenser inventory
        Block block = event.getBlock();
        if (block.getType().equals(Material.DISPENSER)) {
            Dispenser dispenser = (Dispenser) block.getState();
            Inventory dispenserInventory = dispenser.getInventory();

            Directional facingDirection = (Directional) block.getBlockData();
            Block targetBlock = block.getRelative(facingDirection.getFacing());

            // Use an item from the inventory
            for (int i = 0; i < dispenserInventory.getSize(); i++) {
                ItemStack itemStack = dispenserInventory.getItem(i);
                if (itemStack != null && materialInteractions.containsKey(itemStack.getType())) {
                    getLogger().info("Trying to use inventory item: " + itemStack.getType());
                    List<InteractionType> possibleInteractionTypes = materialInteractions.get(itemStack.getType());
                    for (InteractionType interactionType: possibleInteractionTypes) {
                        event.setCancelled(true);
                        if (interactionType.getInteraction().validInteraction(plugin, event, itemStack, targetBlock)) {
                            if (interactionType.getInteraction().performInteraction(plugin, event, itemStack, targetBlock)) {
                                getLogger().info("Performed dispenser action from inventory item: " + itemStack.getType());
                                return;
                            }
                        }
                    }
                }
            }

            getLogger().info("Trying to use event item:" + event.getItem().getType());
            // Use the event item itself
            if (materialInteractions.containsKey(event.getItem().getType())) {
                List<InteractionType> possibleInteractionTypes = materialInteractions.get(event.getItem().getType());
                for (InteractionType interactionType: possibleInteractionTypes) {
                    event.setCancelled(true);
                    if (interactionType.getInteraction().validInteraction(plugin, event, event.getItem(), targetBlock)) {
                        if(interactionType.getInteraction().performInteraction(plugin, event, event.getItem(), targetBlock)) {
                            getLogger().info("Performed dispenser action from inventory item: " + event.getItem().getType());
                            if (interactionType.consumeItem) {
                                scheduleRemoval(dispenserInventory, event.getItem());
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private void scheduleRemoval(Inventory inventory, ItemStack itemToRemove) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                inventory.remove(itemToRemove);
                getLogger().info("Removed item: " + itemToRemove.getType());
            }
        }, 2L);
    }


}
