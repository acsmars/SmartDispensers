package com.acsmars.smartdispensers.interactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

@AllArgsConstructor
public enum InteractionType {
    ATTACK_WITH_WEAPON(new AttackWithWeapon(), "attackwithweapon", false),
    BREAK_WITH_TOOL(new BreakWithTool(), "breakwithtool", false),
    CHARGE_CREEPER(new ChargeCreeper(), "chargecreeper", false),
    COLLECT_FLUID(new CollectFluid(), "collectfluid", true),
    EQUIP_SADDLE(new EquipSaddle(), "equipsaddle", true),
    IGNITE_MOB(new IgniteMob(), "ignitemob", false),
    MILK_COW(new MilkCow(), "milkcow", true),
    PLACE_BLOCK(new PlaceBlock(), "placeblock", true),
    PLANT_CROP(new PlantCrop(), "plantcrop", true),
    SHEAR_SHEEP(new ShearSheep(), "shearsheep", false),
    TILL_SOIL(new TillSoil(), "tillsoil", false);

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
