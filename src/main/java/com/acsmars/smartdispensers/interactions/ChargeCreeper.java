package com.acsmars.smartdispensers.interactions;

import com.acsmars.smartdispensers.SmartDispensers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChargeCreeper extends InteractionImpl implements Interaction {

    private final int creeperRange = 2;

    @Override
    public boolean validInteraction(Plugin plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        return sourceItem.getType().equals(Material.TRIDENT) && sourceItem.getEnchantments().containsKey(Enchantment.CHANNELING);
    }

    @Override
    public boolean performInteraction(SmartDispensers plugin, BlockDispenseEvent event, ItemStack sourceItem, Block targetBlock) {
        List<Creeper> possibleCreepers = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), creeperRange, creeperRange, creeperRange, x -> x instanceof Creeper)
                .stream().map(x -> (Creeper) x).limit(16).collect(Collectors.toList());
        boolean chargedACreeper = false;
        for (Creeper creeper : possibleCreepers) {
            creeper.setPowered(true);
            chargedACreeper = true;
        }
        return chargedACreeper;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.CHARGE_CREEPER;
    }
}
