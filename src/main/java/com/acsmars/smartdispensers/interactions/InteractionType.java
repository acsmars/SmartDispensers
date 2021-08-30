package com.acsmars.smartdispensers.interactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

@AllArgsConstructor
public enum InteractionType {
    BREAK_WITH_TOOL(new BreakWithTool(), "breakwithtool", false),
    PLANT_CROP(new PlantCrop(), "plantcrop", true),
    TILL_SOIL(new TillSoil(), "tillsoil", false),
    PLACE_BLOCK(new PlaceBlock(), "placeblock", true),
    MILK_COW(new MilkCow(), "milkcow", true),
    SHEAR_SHEEP(new ShearSheep(), "shearsheep", false),
    EQUIP_SADDLE(new EquipSaddle(), "equipsaddle", true),
    IGNITE_MOB(new IgniteMob(), "ignitemob", false),
    CHARGE_CREEPER(new ChargeCreeper(), "chargecreeper", false),
    ATTACK_WITH_WEAPON(new AttackWithWeapon(), "attackwithweapon", false);

    @Getter
    public final Interaction interaction;

    @Getter
    public final String name;

    /**
     * True if the event item is consumed by the action.
     * This is used to determine if we should schedule a removal when this item is the event item.
     */
    @Accessors(fluent = true)
    @Getter
    public final boolean consumeItem;

    public static Optional<InteractionType> fromName(String name) {
        for (InteractionType interactionType : InteractionType.values()) {
            if (interactionType.getName().equalsIgnoreCase(name.trim())) {
                return Optional.of(interactionType);
            }
        }
        return Optional.empty();
    }

}
