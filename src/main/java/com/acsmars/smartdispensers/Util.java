package com.acsmars.smartdispensers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {

    public static boolean inventoryHasSpace(Inventory inventory) {
        for (ItemStack stack : inventory.getContents()) {
            if (stack == null) {
                return true;
            }
        }
        return false;
    }
}
