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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bukkit.Bukkit.getLogger;

public class DispenserListener implements Listener {

    Map<Material, List<InteractionType>> materialInteractions;

    SmartDispensers plugin;

    DispenserListener(SmartDispensers plugin, Map<Material, List<InteractionType>> materialInteractions) {
        this.plugin = plugin;
        this.materialInteractions = materialInteractions;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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
                if (itemStack != null && (materialInteractions.containsKey(itemStack.getType()) || itemStack.getType().isBlock())) {
                    List<InteractionType> possibleInteractionTypes = getInteractions(itemStack.getType());
                    boolean wasCancelled = event.isCancelled();
                    for (InteractionType interactionType : possibleInteractionTypes) {
                        event.setCancelled(true);
                        if (interactionType.getInteraction().validInteraction(plugin, event, itemStack, targetBlock)) {
                            if (interactionType.getInteraction().performInteraction(plugin, event, itemStack, targetBlock)) {
                                return;
                            }
                        }
                    }

                    event.setCancelled(wasCancelled);
                }
            }

            // Use the event item itself
            if (materialInteractions.containsKey(event.getItem().getType()) || event.getItem().getType().isBlock()) {
                List<InteractionType> possibleInteractionTypes = getInteractions(event.getItem().getType());
                boolean wasCancelled = event.isCancelled();
                for (InteractionType interactionType : possibleInteractionTypes) {
                    event.setCancelled(true);
                    if (interactionType.getInteraction().validInteraction(plugin, event, event.getItem(), targetBlock)) {
                        if (interactionType.getInteraction().performInteraction(plugin, event, event.getItem(), targetBlock)) {
                            if (interactionType.consumeItem) {
                                scheduleRemoval(dispenserInventory, event.getItem());
                            }
                            return;
                        }
                        wasCancelled = event.isCancelled(); // If we got into a valid interaction with the event item, we don't want to dispense it
                    }
                }
                event.setCancelled(wasCancelled);
            }
        }
    }

    private void scheduleRemoval(Inventory inventory, ItemStack itemToRemove) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (itemToRemove.getAmount() > 1) itemToRemove.setAmount(itemToRemove.getAmount() - 1);
            else inventory.remove(itemToRemove);
        }, 2L);
    }

    private List<InteractionType> getInteractions(Material material) {
        List<InteractionType> possibleInteractionTypes = Optional.ofNullable(materialInteractions.get(material)).orElse(new ArrayList<>());
        if (possibleInteractionTypes.size() == 0 || material.isBlock()) {
            possibleInteractionTypes.add(InteractionType.PLACE_BLOCK);
        }
        return possibleInteractionTypes;
    }


}
