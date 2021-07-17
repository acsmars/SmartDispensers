package com.acsmars.smartdispensers;

import com.acsmars.smartdispensers.interactions.BreakStone;
import com.acsmars.smartdispensers.interactions.Interaction;
import com.acsmars.smartdispensers.interactions.InteractionType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;


public class DispenserListener implements Listener {

    Map<Material, List<InteractionType>> materialInteractions;

    DispenserListener(Map<Material, List<InteractionType>> materialInteractions) {
        this.materialInteractions = materialInteractions;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    private void onDispenserFire(BlockDispenseEvent event) {
        // Check dispenser inventory
        Block block = event.getBlock();
        if (block.getType().equals(Material.DISPENSER)) {
            Dispenser dispenser = (Dispenser) block.getState();
            Inventory dispenserInventory = dispenser.getInventory();
            //getLogger().info("dispenserINV:"+ Arrays.toString(dispenserInventory.getContents()));

            Directional facingDirection = (Directional) block.getBlockData();
            Block targetBlock = block.getRelative(facingDirection.getFacing());

            // Case 1, the last of an interactable item was removed by this event
            if (materialInteractions.containsKey(event.getItem().getType())) {
                List<InteractionType> possibleInteractionTypes = materialInteractions.get(event.getItem().getType());
                for (InteractionType interactionType: possibleInteractionTypes) {
                    if (interactionType.getInteraction().validInteraction(event.getItem(), targetBlock)) {
                        event.setCancelled(true);
                        if (interactionType.useTool()) {
                            interactionType.getInteraction().performInteraction(event.getItem(), targetBlock);
                            return;
                        }
                    }
                }
            }

            // Case 2, there are more items
            for (int i = 0; i < dispenserInventory.getSize(); i++) {
                ItemStack itemStack = dispenserInventory.getItem(i);
                if (itemStack != null && materialInteractions.containsKey(itemStack.getType())) {
                    List<InteractionType> possibleInteractionTypes = materialInteractions.get(itemStack.getType());
                    for (InteractionType interactionType: possibleInteractionTypes) {
                        if (interactionType.getInteraction().performInteraction(itemStack, targetBlock)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }


}
