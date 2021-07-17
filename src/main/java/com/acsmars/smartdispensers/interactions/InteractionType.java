package com.acsmars.smartdispensers.interactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

@AllArgsConstructor
public enum InteractionType {
    BREAK_WOODEN(new BreakWooden(),"breakwooden", true),
    BREAK_STONE(new BreakStone(), "breakstone", true),
    BREAK_DIRT(new BreakDirt(), "breakdirt", true),
    PLANT_CROP(new PlantCrop(), "plantcrop", false),
    TILL_SOIL(new TillSoil(), "tillsoil", true),
    PLACE_BLOCK(new PlaceBlock(), "placeblock", false),
    MILK_COW(new MilkCow(), "milkcow", true),
    EQUIP_SADDLE(new EquipSaddle(), "equipsaddle", false);

    @Getter
    public final Interaction interaction;

    @Getter
    public final String name;

    @Accessors(fluent = true)
    @Getter
    public final boolean useTool;

    public static Optional<InteractionType> fromName(String name) {
        for (InteractionType interactionType : InteractionType.values()) {
            if (interactionType.getName().equalsIgnoreCase(name.trim())) {
                return Optional.of(interactionType);
            }
        }
        return Optional.empty();
    }

}
